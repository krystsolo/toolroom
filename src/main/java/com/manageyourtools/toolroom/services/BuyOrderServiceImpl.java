package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.BuyOrderMapper;
import com.manageyourtools.toolroom.api.model.BuyOrderDTO;
import com.manageyourtools.toolroom.config.AuthenticationFacade;
import com.manageyourtools.toolroom.domains.BuyOrder;
import com.manageyourtools.toolroom.domains.BuyOrderTool;
import com.manageyourtools.toolroom.domains.Tool;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.manageyourtools.toolroom.services.UserDetailsServiceImpl.HAS_ROLE_WAREHOUSEMAN;

@Service
public class BuyOrderServiceImpl implements BuyOrderService {

    private final BuyOrderRepository buyOrderRepository;
    private final AuthenticationFacade authenticationFacade;
    private final EmployeeRepository employeeRepository;
    private final LendingOrderToolRepository lendingOrderToolRepository;
    private final DestructionOrderToolRepository destructionOrderToolRepository;
    private final BuyOrderMapper buyOrderMapper;
    private final ToolRepository toolRepository;

    public BuyOrderServiceImpl(BuyOrderRepository buyOrderRepository, AuthenticationFacade authenticationFacade,
                               EmployeeRepository employeeRepository, LendingOrderToolRepository lendingOrderToolRepository,
                               DestructionOrderToolRepository destructionOrderToolRepository, BuyOrderMapper buyOrderMapper, ToolRepository toolRepository) {
        this.buyOrderRepository = buyOrderRepository;
        this.authenticationFacade = authenticationFacade;
        this.employeeRepository = employeeRepository;
        this.lendingOrderToolRepository = lendingOrderToolRepository;
        this.destructionOrderToolRepository = destructionOrderToolRepository;
        this.buyOrderMapper = buyOrderMapper;
        this.toolRepository = toolRepository;
    }

    @Override
    public Page<BuyOrderDTO> findAllBuyOrders(Pageable pageable) {

        return buyOrderRepository.findAll(pageable).map(buyOrderMapper::buyOrderToBuyOrderDTO);
    }

    @Override
    public BuyOrderDTO findBuyOrderById(Long id) {
        return buyOrderRepository.findById(id).map(buyOrderMapper::buyOrderToBuyOrderDTO).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    @Transactional
    public void deleteBuyOrder(Long id) throws IllegalArgumentException {

        buyOrderRepository.findById(id).ifPresent(buyOrder -> {

            List<BuyOrderTool> buyOrderTools = buyOrder.getBuyOrderTools();

            buyOrderTools.forEach(buyOrderTool -> {
                Tool tool = buyOrderTool.getTool();

                checkIfThereWasAnyDestructionOrLendingOperationAfterBuyOrderOnTool(buyOrder, tool);

                tool = backToToolCountBeforePurchase(buyOrderTool.getCount(), tool);
                buyOrderTool.setTool(tool);
            });
            buyOrder.setBuyOrderTools(buyOrderTools);
            buyOrderRepository.save(buyOrder);
            buyOrderRepository.deleteById(id);
        });
    }


    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public BuyOrderDTO addBuyOrder(BuyOrderDTO buyOrderDTO) throws IllegalArgumentException {

        BuyOrder buyOrder = buyOrderMapper.buyOrderDtoToBuyOrder(buyOrderDTO);

        List<BuyOrderTool> buyOrderTools = buyOrder.getBuyOrderTools();

        buyOrderTools.forEach(buyOrderTool -> {

            Tool tool = toolRepository.findById(buyOrderTool.getTool().getId())
                    .orElseThrow(ResourceNotFoundException::new);

            checkIfToolIsEnable(tool);
            checkIfUniqueToolWillNotBeOverloaded(tool, buyOrderTool.getCount());

            tool = additionToolCount(buyOrderTool.getCount(), tool);
            buyOrderTool.setTool(tool);
        });
        buyOrder.setWarehouseman(
                employeeRepository.findByUserName(
                        authenticationFacade.getUsernameOfCurrentLoggedUser()).orElseThrow(ResourceNotFoundException::new));

        return buyOrderMapper.buyOrderToBuyOrderDTO(buyOrderRepository.save(buyOrder));
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public BuyOrderDTO updateBuyOrder(Long id, BuyOrderDTO buyOrderDTO) {

        Optional<BuyOrder> savedBuyOrderOptional = buyOrderRepository.findById(id);
        if (!savedBuyOrderOptional.isPresent()) {

            return addBuyOrder(buyOrderDTO);
        }

        BuyOrder buyOrder = buyOrderMapper.buyOrderDtoToBuyOrder(buyOrderDTO);

        List<BuyOrderTool> buyOrderTools = buyOrder.getBuyOrderTools();

        buyOrderTools.forEach(buyOrderTool -> {
            long toolId = buyOrderTool.getTool().getId();
            Tool tool = toolRepository.findById(toolId).orElseThrow(ResourceNotFoundException::new);

            checkIfToolIsEnable(tool);
            checkIfUniqueToolWillNotBeOverloaded(tool, buyOrderTool.getCount());
            buyOrderTool.setTool(tool);
        });

        BuyOrder savedBuyOrder = savedBuyOrderOptional.get();
        List<BuyOrderTool> savedBuyOrderTools = savedBuyOrder.getBuyOrderTools();

        List<BuyOrderTool> buyOrderToolFiltered = savedBuyOrderTools
                .stream()
                .filter(savedBuyOrderTool -> {
                    boolean isToolNewInOrderOrCountIsNotLowerFromPreviousSaved = true;
                    for (BuyOrderTool buyOrderTool : buyOrderTools) {
                        if ((buyOrderTool.getId().equals(savedBuyOrderTool.getId()) &&
                                buyOrderTool.getCount() >= savedBuyOrderTool.getCount())) {
                            isToolNewInOrderOrCountIsNotLowerFromPreviousSaved = false;
                        }
                    }
                    return isToolNewInOrderOrCountIsNotLowerFromPreviousSaved;
                }).collect(Collectors.toList());

        buyOrderToolFiltered.forEach(buyOrderTool ->
                checkIfThereWasAnyDestructionOrLendingOperationAfterBuyOrderOnTool(buyOrder, buyOrderTool.getTool()));

        savedBuyOrderTools.forEach(savedBuyOrderTool -> {
                    Tool tool = backToToolCountBeforePurchase(savedBuyOrderTool.getCount(), savedBuyOrderTool.getTool());
                    toolRepository.save(tool);
                }
        );


        buyOrderTools.forEach(buyOrderTool -> {
                    Tool tool = toolRepository.findById(buyOrderTool.getTool().getId()).orElseThrow(ResourceNotFoundException::new);
                    tool = additionToolCount(buyOrderTool.getCount(), tool);
                    buyOrderTool.setTool(tool);
                }
        );
        buyOrder.setBuyOrderTools(buyOrderTools);

        return buyOrderMapper.buyOrderToBuyOrderDTO(buyOrderRepository.save(buyOrder));
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public BuyOrderDTO patchBuyOrder(Long id, BuyOrderDTO buyOrderDTO) {
        //todo I'm not sure if patch method will be necessary here
        return null;
    }

    protected void checkIfUniqueToolWillNotBeOverloaded(Tool tool, long addCount) throws IllegalArgumentException {
        if (tool.getIsUnique() && (tool.getAllCount() + addCount > 0)) {
            throw new IllegalArgumentException("Cannot add to " + tool.getName() + " this number of tools, because it has unique flag");
        }
    }

    protected Tool backToToolCountBeforePurchase(Long toolCountFromOrder, Tool tool) {

        tool.setCurrentCount(tool.getCurrentCount() - toolCountFromOrder);
        tool.setAllCount(tool.getAllCount() - toolCountFromOrder);
        return tool;
    }

    protected void checkIfThereWasAnyDestructionOrLendingOperationAfterBuyOrderOnTool(BuyOrder buyOrder, Tool tool) throws IllegalArgumentException {
        Timestamp additionToBuyOrder = buyOrder.getAddTimestamp();

        if (isWasAnyDestructionOperationAfterBuyOrderOnTool(tool, additionToBuyOrder) || isWasAnyLendingOperationAfterBuyOrderOnTool(tool, additionToBuyOrder)) {
            throw new IllegalArgumentException("Cannot delete Buy Order " + buyOrder.getId() + " because added tools to warehouse can be in used");
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

    protected Tool additionToolCount(Long toolCountFromOrder, Tool tool) {

        tool.setCurrentCount(tool.getCurrentCount() + toolCountFromOrder);
        tool.setAllCount(tool.getAllCount() + toolCountFromOrder);
        return tool;
    }

    protected void checkIfToolIsEnable(Tool tool) throws IllegalArgumentException {
        if (!tool.getIsEnable()) {
            throw new IllegalArgumentException("Cannot add " + tool.getName() + " tool, because it was disabled");
        }
    }

}
