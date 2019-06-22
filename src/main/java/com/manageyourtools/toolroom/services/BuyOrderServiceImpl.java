package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.BuyOrderMapper;
import com.manageyourtools.toolroom.api.model.BuyOrderDTO;
import com.manageyourtools.toolroom.config.AuthenticationFacade;
import com.manageyourtools.toolroom.domains.BuyOrder;
import com.manageyourtools.toolroom.domains.BuyOrderTool;
import com.manageyourtools.toolroom.domains.Tool;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.manageyourtools.toolroom.services.UserDetailsServiceImpl.HAS_ROLE_WAREHOUSEMAN;

@Service
@Slf4j
public class BuyOrderServiceImpl implements BuyOrderService {

    private final BuyOrderRepository buyOrderRepository;
    private final AuthenticationFacade authenticationFacade;
    private final EmployeeService employeeService;
    private final LendingOrderToolRepository lendingOrderToolRepository;
    private final DestructionOrderToolRepository destructionOrderToolRepository;
    private final BuyOrderMapper buyOrderMapper;
    private final ToolService toolService;
    private final ToolRepository toolRepository;

    public BuyOrderServiceImpl(BuyOrderRepository buyOrderRepository, AuthenticationFacade authenticationFacade,
                               EmployeeService employeeService, LendingOrderToolRepository lendingOrderToolRepository,
                               DestructionOrderToolRepository destructionOrderToolRepository, BuyOrderMapper buyOrderMapper, ToolService toolService, ToolRepository toolRepository) {
        this.buyOrderRepository = buyOrderRepository;
        this.authenticationFacade = authenticationFacade;
        this.employeeService = employeeService;
        this.lendingOrderToolRepository = lendingOrderToolRepository;
        this.destructionOrderToolRepository = destructionOrderToolRepository;
        this.buyOrderMapper = buyOrderMapper;
        this.toolService = toolService;
        this.toolRepository = toolRepository;
    }

    @Override
    public List<BuyOrderDTO> findAllBuyOrders() {

        return buyOrderRepository.findAll().stream().map(buyOrderMapper::buyOrderToBuyOrderDTO).collect(Collectors.toList());
    }

    @Override
    public BuyOrderDTO findBuyOrderById(Long id) {
        return buyOrderRepository.findById(id)
                .map(buyOrderMapper::buyOrderToBuyOrderDTO)
                .orElseThrow(() -> new ResourceNotFoundException("There is no buy order with id=" + id));
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    @Transactional
    public void deleteBuyOrder(Long id) throws IllegalArgumentException {

        buyOrderRepository.findById(id).ifPresent(buyOrder -> {
            this.restoreToolsToStateBeforePreviousOrder(buyOrder);
            buyOrderRepository.deleteById(id);
        });
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    @Transactional
    public BuyOrderDTO addBuyOrder(BuyOrderDTO buyOrderDTO) throws IllegalArgumentException {
        BuyOrder buyOrder = buyOrderMapper.buyOrderDtoToBuyOrder(buyOrderDTO);
        return buyOrderMapper.buyOrderToBuyOrderDTO(saveBuyOrder(buyOrder));
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    @Transactional
    public BuyOrderDTO updateBuyOrder(Long id, BuyOrderDTO buyOrderDTO) {

        Optional<BuyOrder> savedBuyOrderOptional = buyOrderRepository.findById(id);
        if (!savedBuyOrderOptional.isPresent()) {
            return addBuyOrder(buyOrderDTO);
        }

        BuyOrder buyOrder = buyOrderMapper.buyOrderDtoToBuyOrder(buyOrderDTO);

        return buyOrderMapper.buyOrderToBuyOrderDTO(changeBuyOrder(savedBuyOrderOptional.get(), buyOrder));
    }

    protected BuyOrder changeBuyOrder(BuyOrder savedBuyOrder, BuyOrder buyOrder) {

        this.restoreToolsToStateBeforePreviousOrder(savedBuyOrder);
        this.checkAndCountToolsFromOrder(buyOrder);

        buyOrder.setWarehouseman(this.employeeService.getLoggedEmployee());
        buyOrder.setId(savedBuyOrder.getId());
        return buyOrderRepository.save(buyOrder);
    }

    protected void restoreToolsToStateBeforePreviousOrder(BuyOrder buyOrder) {
        buyOrder.getBuyOrderTools()
                .forEach(buyOrderTool -> {
                    Tool tool = buyOrderTool.getTool();
                    this.checkIfToolIsEnable(tool);
                    this.checkIfThereWasNoDestructionOrLendingOperationAfterBuyOrderOnTool(buyOrder.getAddTimestamp(), tool);
                    this.backToToolCountBeforePurchase(buyOrderTool.getCount(), tool);
                });
    }

    protected void checkAndCountToolsFromOrder(BuyOrder buyOrder) {

        Set<BuyOrderTool> buyOrderTools = new HashSet<>(buyOrder.getBuyOrderTools());

        checkIfThereIsNoToolRepeat(buyOrderTools);

        buyOrderTools.forEach(buyOrderTool -> {
            long toolId = buyOrderTool.getTool().getId();
            Tool tool = toolService.findToolById(toolId);

            this.checkIfToolIsEnable(tool);
            this.checkIfUniqueToolWillNotBeOverloaded(tool, buyOrderTool.getCount());
            this.calculateToolCount(buyOrderTool.getCount(), tool);

            tool.addBuyOrderTool(buyOrderTool);
            buyOrder.addBuyOrderTool(buyOrderTool);
        });
    }

    protected BuyOrder saveBuyOrder(BuyOrder buyOrder) {
        this.checkAndCountToolsFromOrder(buyOrder);

        buyOrder.setWarehouseman(
                this.employeeService.getLoggedEmployee());

        return buyOrderRepository.save(buyOrder);
    }

    protected void decreaseToolCountIfWasInPreviousBuyOrder(Set<BuyOrderTool> previousBuyOrderTools, Tool tool) {
        previousBuyOrderTools.stream()
                .filter(buyOrderTool -> buyOrderTool.getTool().getId().equals(tool.getId()))
                .findFirst()
                .ifPresent(buyOrderTool -> backToToolCountBeforePurchase(buyOrderTool.getCount(), tool));
        log.info("tool all count during=" + tool.getAllCount());

    }

    protected void checkIfUniqueToolWillNotBeOverloaded(Tool tool, long addCount) throws IllegalArgumentException {
        if (tool.getIsUnique() && (tool.getAllCount() + addCount > 1)) {
            throw new IllegalArgumentException("Cannot add to " + tool.getName() + " this number of tools, because it has unique flag");
        }
    }

    protected void backToToolCountBeforePurchase(Long toolCountFromOrder, Tool tool) {
        tool.setCurrentCount(tool.getCurrentCount() - toolCountFromOrder);
        tool.setAllCount(tool.getAllCount() - toolCountFromOrder);
    }

    protected void checkIfThereWasNoDestructionOrLendingOperationAfterBuyOrderOnTool(Timestamp addTime, Tool tool) throws IllegalArgumentException {
        if (isWasAnyDestructionOperationAfterBuyOrderOnTool(tool, addTime) || isWasAnyLendingOperationAfterBuyOrderOnTool(tool, addTime)) {
            throw new IllegalArgumentException("Cannot do operation on Buy Order, because for tool " + tool.getName() + " there are lending or destruction orders after buy order addition");
        }
    }

    protected boolean isWasAnyLendingOperationAfterBuyOrderOnTool(Tool tool, Timestamp additionToBuyOrder) {
        return lendingOrderToolRepository.findAllByTool(tool)
                .stream()
                .anyMatch(lendingOrderTool -> lendingOrderTool.getLendingOrder().getPickTime().after(additionToBuyOrder));
    }

    protected boolean isWasAnyDestructionOperationAfterBuyOrderOnTool(Tool tool, Timestamp additionToBuyOrder) {
        return destructionOrderToolRepository.findAllByTool(tool)
                .stream()
                .anyMatch(destructionOrderTool -> destructionOrderTool.getDestructionOrder().getAddTimestamp().after(additionToBuyOrder));
    }

    protected void calculateToolCount(Long toolCountFromOrder, Tool tool) {
        tool.setCurrentCount(tool.getCurrentCount() + toolCountFromOrder);
        tool.setAllCount(tool.getAllCount() + toolCountFromOrder);
    }

    protected void checkIfToolIsEnable(Tool tool) throws IllegalArgumentException {
        if (!tool.getIsEnable()) {
            throw new IllegalArgumentException("Cannot add " + tool.getName() + " tool, because it was disabled");
        }
    }

    protected void checkIfThereIsNoToolRepeat(Set<BuyOrderTool> buyOrderTools) {
        Set<Long> tools = buyOrderTools.stream().map(buyOrderTool -> buyOrderTool.getTool().getId()).collect(Collectors.toSet());
        if (!containsUnique(tools)) {
            throw new IllegalArgumentException("There are more than one buyOrderTool for one specific tool");
        }
    }

    protected <T> boolean containsUnique(Set<T> set) {
        return set.stream().allMatch(new HashSet<>()::add);
    }

}
