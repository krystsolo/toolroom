package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.LendingOrderMapper;
import com.manageyourtools.toolroom.api.model.LendingOrderDTO;
import com.manageyourtools.toolroom.config.AuthenticationFacade;
import com.manageyourtools.toolroom.domains.*;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.LendingOrderRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.manageyourtools.toolroom.services.UserDetailsServiceImpl.HAS_ROLE_WAREHOUSEMAN;

@Service
public class LendingOrderService {

    private final LendingOrderRepository lendingOrderRepository;
    private final LendingOrderMapper lendingOrderMapper;
    private final AuthenticationFacade authenticationFacade;
    private final EmployeeService employeeService;
    private final ToolService toolService;

    public LendingOrderService(LendingOrderRepository lendingOrderRepository, LendingOrderMapper lendingOrderMapper, AuthenticationFacade authenticationFacade,
                               EmployeeService employeeService, ToolService toolService) {
        this.lendingOrderRepository = lendingOrderRepository;
        this.lendingOrderMapper = lendingOrderMapper;
        this.authenticationFacade = authenticationFacade;
        this.employeeService = employeeService;
        this.toolService = toolService;
    }

    public List<LendingOrderDTO> findAllLendingOrders() {
        return lendingOrderRepository.findAll().stream()
                .map(lendingOrderMapper::lendingOrderToLendingOrderDTO)
                .collect(Collectors.toList());
    }

    public LendingOrderDTO findLendingOrderDTOById(Long id) {
        return lendingOrderMapper.lendingOrderToLendingOrderDTO(findLendingOrderById(id));
    }

    public LendingOrder findLendingOrderById(Long id) {
        return lendingOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lending order with id=" +  id + " not found"));
    }

    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    @Transactional
    public void deleteLendingOrder(Long id) {
        lendingOrderRepository.findById(id)
                .ifPresent(lendingOrder -> {
                    List<Long> returnedToolsId = lendingOrder.getLendingReturnOrder().getLendingReturnOrderTools()
                            .stream()
                            .filter(LendingReturnOrderTool::getIsReturned)
                            .map(l -> l.getTool().getId())
                            .collect(Collectors.toList());

                    lendingOrder.getLendingOrderTools().stream()
                            .filter(l -> !returnedToolsId.contains(l.getTool().getId()))
                            .forEach(lendingOrderTool ->
                                    backToToolCountBeforeOrder(lendingOrderTool.getCount(), lendingOrderTool.getTool())
                            );
                    lendingOrderRepository.deleteById(id);
                });
    }

    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    @Transactional
    public LendingOrderDTO addLendingOrder(LendingOrderDTO lendingOrderDTO) {
        LendingOrder lendingOrder = lendingOrderMapper.lendingOrderDTOToLendingOrder(lendingOrderDTO);
        LendingOrder savedLendingOrder = saveLendingOrder(lendingOrder);
        return lendingOrderMapper.lendingOrderToLendingOrderDTO(savedLendingOrder);
    }

    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    @Transactional
    public LendingOrderDTO updateLendingOrder(Long id, LendingOrderDTO lendingOrderDTO) {
        Optional<LendingOrder> previousLendingOrder = this.lendingOrderRepository.findById(id);
        if (!previousLendingOrder.isPresent()) {
            return this.addLendingOrder(lendingOrderDTO);
        }

        LendingOrder lendingOrder = lendingOrderMapper.lendingOrderDTOToLendingOrder(lendingOrderDTO);
        LendingOrder savedLendingOrder = changeFromPreviousLendingOrderToNew(previousLendingOrder.get(), lendingOrder);
        return lendingOrderMapper.lendingOrderToLendingOrderDTO(savedLendingOrder);
    }

    private LendingOrder saveLendingOrder(LendingOrder lendingOrder) {
        createLendingOrder(lendingOrder);
        return lendingOrderRepository.save(lendingOrder);
    }

    private void createLendingOrder(LendingOrder lendingOrder) {
        makeBidirectionalRelations(lendingOrder);
        lendingOrder.getLendingOrderTools().forEach(this::checkAndCalculateToolCount);
        Employee worker = employeeService.getEmployeeById(lendingOrder.getWorker().getId());
        checkIfEmployeeIsActive(worker);
        lendingOrder.setWorker(worker);
        Employee warehouseman = employeeService.getLoggedEmployee();
        lendingOrder.setWarehouseman(warehouseman);

        Set<LendingOrderTool> toReturn =
                getLendingToolsToReturn(lendingOrder.getLendingOrderTools());

        if (toReturn.size() > 0) {
            LendingReturnOrder lendingReturnOrder = new LendingReturnOrder();
            lendingReturnOrder.setOrderNumber(lendingOrder.getOrderNumber());
            lendingReturnOrder.setReturnUntilTime(lendingOrder.getReturnUntilTime());
            lendingReturnOrder.setWorker(worker);
            toReturn.forEach(lendingOrderTool -> {
                LendingReturnOrderTool lendingReturnOrderTool = new LendingReturnOrderTool();
                lendingReturnOrderTool.setCount(lendingOrderTool.getCount());
                lendingOrderTool.getTool().addLendingReturnOrderTool(lendingReturnOrderTool);
                lendingReturnOrder.addLendingReturnOrderTool(lendingReturnOrderTool);
            });
            lendingOrder.addLendingReturnOrder(lendingReturnOrder);
        } else {
            lendingOrder.setLendingReturnOrder(null);
            lendingOrder.setReturnUntilTime(null);
        }
    }

    private void checkIfEmployeeIsActive(Employee worker) {
        if (!worker.getIsActive()) {
            throw new IllegalArgumentException("Worker must be active! Employee=" + worker.getUserName() + " is inactive");
        }
    }

    private Set<LendingOrderTool> getLendingToolsToReturn(Set<LendingOrderTool> lendingOrderTools) {
        return lendingOrderTools
                .stream()
                .filter(lendingOrderTool -> lendingOrderTool.getTool().getIsToReturn())
                .collect(Collectors.toSet());
    }

    private void makeBidirectionalRelations(LendingOrder lendingOrder) {
        List<LendingOrderTool> lendingOrderTools = new ArrayList<>(lendingOrder.getLendingOrderTools());
        lendingOrder.setLendingOrderTools(new HashSet<>());
        lendingOrderTools.forEach(lendingOrderTool -> {
            Tool tool = toolService.findToolById(lendingOrderTool.getTool().getId());
            tool.addLendingOrderTool(lendingOrderTool);
            lendingOrder.addLendingOrderTool(lendingOrderTool);
        });
    }

    private void checkAndCalculateToolCount(LendingOrderTool lendingOrderTool) {
        Tool tool = lendingOrderTool.getTool();
        checkIfToolIsEnable(tool);
        long count = lendingOrderTool.getCount();
        if (tool.getCurrentCount() < count) {
            throw new IllegalArgumentException("Cannot lend " + tool.getName() + " tool, because lend count is bigger then number of tool in warehouse");
        }
        tool.setCurrentCount(tool.getCurrentCount() - count);
        if (!tool.getIsToReturn()) {
            tool.setAllCount(tool.getAllCount() - count);
        }
    }

    private LendingOrder changeFromPreviousLendingOrderToNew(LendingOrder previousLendingOrder, LendingOrder newLendingOrder) {
        checkIfAnyToolWasNotReturnedInPreviouslyAddedOrder(previousLendingOrder.getLendingReturnOrder());
        restoreToolsStateToBeforePreviousLendingOrder(previousLendingOrder);
        createLendingOrder(newLendingOrder);
        newLendingOrder.getLendingReturnOrder().setId(previousLendingOrder.getLendingReturnOrder().getId());
        newLendingOrder.setId(previousLendingOrder.getId());
        return lendingOrderRepository.save(newLendingOrder);
    }

    private void restoreToolsStateToBeforePreviousLendingOrder(LendingOrder previousLendingOrder) {
        previousLendingOrder.getLendingOrderTools()
                .forEach(lendingOrderTool -> {
                    Tool tool = lendingOrderTool.getTool();
                    checkIfToolIsEnable(tool);
                    this.backToToolCountBeforeOrder(lendingOrderTool.getCount(), tool);
                });
    }

    private void checkIfAnyToolWasNotReturnedInPreviouslyAddedOrder(LendingReturnOrder lendingReturnOrder) {
        lendingReturnOrder.getLendingReturnOrderTools().stream()
                .filter(LendingReturnOrderTool::getIsReturned)
                .findFirst()
                .ifPresent(a -> {
                    throw new IllegalArgumentException("Tool " + a.getTool().getName() + " was already returned so lending order cannot be updated!");
                });
    }

    private void checkIfToolIsEnable(Tool tool) {
        if (!tool.getIsEnable()) {
            throw new IllegalArgumentException("Cannot lend " + tool.getName() + " tool, because it was disabled");
        }
    }

    protected void backToToolCountBeforeOrder(int toolCountFromOrder, Tool tool) {
        tool.setCurrentCount(tool.getCurrentCount() + toolCountFromOrder);
        if (!tool.getIsToReturn()) {
            tool.setAllCount(tool.getAllCount() + toolCountFromOrder);
        }

    }
}
