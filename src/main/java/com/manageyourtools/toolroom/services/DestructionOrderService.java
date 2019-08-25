package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.DestructionOrderMapper;
import com.manageyourtools.toolroom.api.model.DestructionOrderDTO;
import com.manageyourtools.toolroom.config.AuthenticationFacade;
import com.manageyourtools.toolroom.domains.DestructionOrder;
import com.manageyourtools.toolroom.domains.DestructionOrderTool;
import com.manageyourtools.toolroom.domains.Tool;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.DestructionOrderRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.manageyourtools.toolroom.services.UserDetailsServiceImpl.HAS_ROLE_WAREHOUSEMAN;

@Service
public class DestructionOrderService {

    private final DestructionOrderRepository destructionOrderRepository;
    private final DestructionOrderMapper destructionOrderMapper;
    private final EmployeeService employeeService;
    private final ToolService toolService;

    public DestructionOrderService(DestructionOrderRepository destructionOrderRepository, DestructionOrderMapper destructionOrderMapper,
                                   EmployeeService employeeService, ToolService toolService) {
        this.destructionOrderRepository = destructionOrderRepository;
        this.destructionOrderMapper = destructionOrderMapper;
        this.employeeService = employeeService;
        this.toolService = toolService;
    }

    public List<DestructionOrderDTO> findAllDestructionOrders() {
        return destructionOrderRepository.findAll()
                .stream()
                .map(destructionOrderMapper::destructionOrderToDestructionOrderDto)
                .collect(Collectors.toList());
    }

    public DestructionOrderDTO findDestructionOrderById(Long id) {
        return destructionOrderRepository.findById(id)
                .map(destructionOrderMapper::destructionOrderToDestructionOrderDto)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Destruction order with id=" + id + " not found"));
    }

    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    @Transactional
    public void deleteDestructionOrder(Long id) {
        this.destructionOrderRepository.findById(id)
                .ifPresent(destructionOrder ->
                        destructionOrder.getDestructionOrderTools()
                                .forEach(destructionOrderTool ->
                                        undoDestructionChangesOnTool(destructionOrderTool.getTool(), destructionOrderTool.getCount()))
                );
        destructionOrderRepository.deleteById(id);
    }

    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    @Transactional
    public DestructionOrderDTO addDestructionOrder(DestructionOrderDTO destructionOrderDTO) {
        DestructionOrder destructionOrder = destructionOrderMapper.destructionOrderDtoToDestructionOrder(destructionOrderDTO);
        DestructionOrder savedDestructionOrder = saveDestructionOrder(destructionOrder);
        return destructionOrderMapper.destructionOrderToDestructionOrderDto(savedDestructionOrder);
    }

    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    @Transactional
    public DestructionOrderDTO updateDestructionOrder(Long id, DestructionOrderDTO destructionOrderDTO) {
        Optional<DestructionOrder> destructionOrderOptional = destructionOrderRepository.findById(id);
        if (!destructionOrderOptional.isPresent()) {
            return addDestructionOrder(destructionOrderDTO);
        }

        DestructionOrder destructionOrder = destructionOrderMapper.destructionOrderDtoToDestructionOrder(destructionOrderDTO);
        DestructionOrder savedDestructionOrder = changeFromPreviousDestructionOrderToNew(destructionOrderOptional.get(), destructionOrder);
        return destructionOrderMapper.destructionOrderToDestructionOrderDto(savedDestructionOrder);
    }

    protected DestructionOrder saveDestructionOrder(DestructionOrder destructionOrder) {
        checkAndCountToolsAfterDestructionOrder(destructionOrder);
        destructionOrder.setWarehouseman(
                this.employeeService.getLoggedEmployee());
        return destructionOrderRepository.save(destructionOrder);
    }

    protected DestructionOrder changeFromPreviousDestructionOrderToNew(DestructionOrder previousDestructionOrder, DestructionOrder newDestructionOrder) {
        restoreStateBeforeDestructionOrder(previousDestructionOrder);
        checkAndCountToolsAfterDestructionOrder(newDestructionOrder);
        newDestructionOrder.setWarehouseman(employeeService.getLoggedEmployee());
        newDestructionOrder.setId(previousDestructionOrder.getId());
        return destructionOrderRepository.save(newDestructionOrder);
    }

    protected void restoreStateBeforeDestructionOrder(DestructionOrder previousDestructionOrder) {
        previousDestructionOrder.getDestructionOrderTools()
                .forEach(destructionOrderTool -> {
                    Tool tool = destructionOrderTool.getTool();
                    undoDestructionChangesOnTool(tool, destructionOrderTool.getCount());
                });
    }

    protected void checkAndCountToolsAfterDestructionOrder(DestructionOrder newDestructionOrder) {
        Set<DestructionOrderTool> destructionOrderTools = new HashSet<>(newDestructionOrder.getDestructionOrderTools());
        destructionOrderTools
                .forEach(destructionOrderTool -> {
                    long toolId = destructionOrderTool.getTool().getId();
                    Tool tool = toolService.findToolById(toolId);

                    checkIfToolIsEnable(tool);
                    long count = destructionOrderTool.getCount();
                    if (tool.getAllCount() < count || tool.getCurrentCount() < count) {
                        throw new IllegalArgumentException("Cannot destroy " + tool.getName() + " tool, because destroy count was bigger then number of tool");
                    }
                    tool.setCurrentCount(tool.getCurrentCount() - count);
                    tool.setAllCount(tool.getAllCount() - count);
                    this.setIsEnableIfToolIsUnique(tool, false);
                    tool.addDestructionOrderTool(destructionOrderTool);
                    newDestructionOrder.addDestroyedTool(destructionOrderTool);
                });
    }

    protected void checkIfToolIsEnable(Tool tool) {
        if (!tool.getIsEnable()) {
            throw new IllegalArgumentException("Cannot destroy " + tool.getName() + " tool, because it was disabled");
        }
    }

    protected void setIsEnableIfToolIsUnique(Tool tool, boolean isEnable) {
        if (tool.getIsUnique()) {
            tool.setIsEnable(isEnable);
        }
    }

    protected void undoDestructionChangesOnTool(Tool tool, long count) {
        if (!tool.getIsUnique() && !tool.getIsEnable()) {
            throw new IllegalArgumentException("Cannot update destruction order, because " + tool.getName() + " tool was disabled");
        }

        tool.setCurrentCount(tool.getCurrentCount() + count);
        tool.setAllCount(tool.getAllCount() + count);
        this.setIsEnableIfToolIsUnique(tool, true);
    }
}
