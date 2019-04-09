package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.LendingReturnOrderMapper;
import com.manageyourtools.toolroom.api.model.LendingReturnOrderDTO;
import com.manageyourtools.toolroom.config.AuthenticationFacade;
import com.manageyourtools.toolroom.domains.*;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.LendingReturnOrderRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LendingReturnOrderServiceImpl implements LendingReturnOrderService {

    private final LendingReturnOrderRepository lendingReturnOrderRepository;
    private final LendingReturnOrderMapper lendingReturnOrderMapper;
    private final ToolService toolService;
    private final EmployeeService employeeService;

    public LendingReturnOrderServiceImpl(LendingReturnOrderRepository lendingReturnOrderRepository, LendingReturnOrderMapper lendingReturnOrderMapper,
                                         ToolService toolService, EmployeeService employeeService) {
        this.lendingReturnOrderRepository = lendingReturnOrderRepository;
        this.lendingReturnOrderMapper = lendingReturnOrderMapper;
        this.toolService = toolService;
        this.employeeService = employeeService;
    }

    @Override
    public LendingReturnOrderDTO returnLendingOrder(Long id, LendingReturnOrderDTO lendingReturnOrderDTO) {

        LendingReturnOrder previousLendingReturnOrder = getLendingReturnOrder(id);

        LendingReturnOrder lendingReturnOrder = lendingReturnOrderMapper.lendingReturnOrderDTOToLendingReturnOrder(lendingReturnOrderDTO);
        LendingReturnOrder savedLendingReturnOrder = saveLendingReturnOrder(previousLendingReturnOrder, lendingReturnOrder);
        return lendingReturnOrderMapper.lendingReturnOrderToLendingReturnOrderDTO(savedLendingReturnOrder);
    }

    private LendingReturnOrder saveLendingReturnOrder(LendingReturnOrder previousLendingReturnOrder, LendingReturnOrder lendingReturnOrder) {
        lendingReturnOrder.setId(previousLendingReturnOrder.getId());

        List<LendingReturnOrderTool> lendingReturnOrderTools = new ArrayList<>(lendingReturnOrder.getLendingReturnOrderTools());
        Employee warehouseman = employeeService.getLoggedEmployee();

        lendingReturnOrderTools.forEach(lendingReturnOrderTool -> {
                    Tool tool = toolService.findToolById(lendingReturnOrderTool.getTool().getId());
                    tool.addLendingReturnOrderTool(lendingReturnOrderTool);
                    lendingReturnOrder.addLendingReturnOrderTool(lendingReturnOrderTool);

                    LendingReturnOrderTool oldOrderTool = findLendingReturnOrderToolFromList(lendingReturnOrderTool, previousLendingReturnOrder.getLendingReturnOrderTools());

                    if (isLendingOrderToolStatusChanged(lendingReturnOrderTool, oldOrderTool)) {
                        if (lendingReturnOrderTool.getIsReturned()) {
                            tool.setCurrentCount(tool.getCurrentCount() + lendingReturnOrderTool.getCount());
                            lendingReturnOrderTool.setReturnDate(LocalDateTime.now());
                            lendingReturnOrderTool.setReturnWarehouseman(warehouseman);

                        } else {
                            checkIfIsEnoughToolsInWarehouseAndIsNotDisable(lendingReturnOrderTool);
                            tool.setCurrentCount(tool.getCurrentCount() - lendingReturnOrderTool.getCount());
                            lendingReturnOrderTool.setReturnDate(null);
                            lendingReturnOrderTool.setReturnWarehouseman(null);
                        }
                    }
                }
        );

        lendingReturnOrder.setOrderNumber(previousLendingReturnOrder.getOrderNumber());
        lendingReturnOrder.setReturnWarehouseman(warehouseman);
        lendingReturnOrder.setId(previousLendingReturnOrder.getId());
        lendingReturnOrder.setWorker(previousLendingReturnOrder.getWorker());
        if (isAllToolsReturned(lendingReturnOrderTools)) {
            lendingReturnOrder.setIsReturned(true);
            lendingReturnOrder.setReturnTime(LocalDateTime.now());
        } else {
            lendingReturnOrder.setIsReturned(false);
            lendingReturnOrder.setReturnTime(null);
        }
        return lendingReturnOrderRepository.save(lendingReturnOrder);
    }

    private void checkIfIsEnoughToolsInWarehouseAndIsNotDisable(LendingReturnOrderTool lendingReturnOrderTool) {
        Tool tool = lendingReturnOrderTool.getTool();
        if (!tool.getIsEnable() ) {
            throw new IllegalArgumentException("Cannot change lend return " + tool.getName() + " tool, because it was disabled");
        }
        if (tool.getCurrentCount() < lendingReturnOrderTool.getCount()) {
            throw new IllegalArgumentException("Cannot change lend return " + tool.getName() + " tool, because lend count is bigger then number of tool in warehouse");
        }
    }

    private boolean isAllToolsReturned(List<LendingReturnOrderTool> lendingReturnOrderTools) {
        return lendingReturnOrderTools.stream().allMatch(LendingReturnOrderTool::getIsReturned);
    }

    private LendingReturnOrderTool findLendingReturnOrderToolFromList(LendingReturnOrderTool lendingReturnOrderTool, List<LendingReturnOrderTool> lendingReturnOrderTools) {
        return lendingReturnOrderTools.stream()
                .filter(l -> l.getTool().getId().equals(lendingReturnOrderTool.getTool().getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("There is no lending return order tool in database"));
    }

    private boolean isLendingOrderToolStatusChanged(LendingReturnOrderTool newOrderTool, LendingReturnOrderTool oldOrderTool) {
        return newOrderTool.getIsReturned() != oldOrderTool.getIsReturned();
    }


    @Override
    public LendingReturnOrderDTO getLendingReturnOrderDTO(Long id) {
        return lendingReturnOrderMapper.lendingReturnOrderToLendingReturnOrderDTO(this.getLendingReturnOrder(id));
    }

    public LendingReturnOrder getLendingReturnOrder(Long id) {
        return lendingReturnOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LendingReturnOrder with id=" + id + " not found"));
    }

    @Override
    public List<LendingReturnOrderDTO> getAllOrders() {
        return lendingReturnOrderRepository.findAll().stream()
                .map(lendingReturnOrderMapper::lendingReturnOrderToLendingReturnOrderDTO)
                .collect(Collectors.toList());
    }
}
