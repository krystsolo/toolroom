package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.DestructionOrderMapper;
import com.manageyourtools.toolroom.api.model.DestructionOrderDTO;
import com.manageyourtools.toolroom.config.AuthenticationFacade;
import com.manageyourtools.toolroom.domains.DestructionOrder;
import com.manageyourtools.toolroom.domains.DestructionOrderTool;
import com.manageyourtools.toolroom.domains.Employee;
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
public class DestructionOrderServiceImpl implements DestructionOrderService {

    private final DestructionOrderRepository destructionOrderRepository;
    private final DestructionOrderMapper destructionOrderMapper;
    private final AuthenticationFacade authenticationFacade;
    private final EmployeeService employeeService;
    private final ToolService toolService;

    public DestructionOrderServiceImpl(DestructionOrderRepository destructionOrderRepository, DestructionOrderMapper destructionOrderMapper, AuthenticationFacade authenticationFacade, EmployeeService employeeService, ToolService toolService) {
        this.destructionOrderRepository = destructionOrderRepository;
        this.destructionOrderMapper = destructionOrderMapper;
        this.authenticationFacade = authenticationFacade;
        this.employeeService = employeeService;
        this.toolService = toolService;
    }

    @Override
    public List<DestructionOrderDTO> findAllDestructionOrders() {
        return destructionOrderRepository.findAll().stream().map(destructionOrderMapper::destructionOrderToDestructionOrderDto).collect(Collectors.toList());
    }

    @Override
    public DestructionOrderDTO findDestructionOrderById(Long id) {
        return destructionOrderRepository.findById(id).map(destructionOrderMapper::destructionOrderToDestructionOrderDto).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    @Transactional
    public void deleteDestructionOrder(Long id) {
        this.destructionOrderRepository.findById(id)
                .ifPresent(destructionOrder -> destructionOrder.getDestructionOrderTools()
                        .forEach(destructionOrderTool ->
                                undoDestructionChangesOnTool(destructionOrderTool.getTool(), destructionOrderTool.getCount()))
                );
        destructionOrderRepository.deleteById(id);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public DestructionOrderDTO addDestructionOrder(DestructionOrderDTO destructionOrderDTO) {

        DestructionOrder destructionOrder = destructionOrderMapper.destructionOrderDtoToDestructionOrder(destructionOrderDTO);
        DestructionOrder savedDestructionOrder = saveDestructionOrder(destructionOrder);
        return destructionOrderMapper.destructionOrderToDestructionOrderDto(savedDestructionOrder);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public DestructionOrderDTO updateDestructionOrder(Long id, DestructionOrderDTO destructionOrderDTO) {

        Optional<DestructionOrder> destructionOrderOptional = destructionOrderRepository.findById(id);
        if (!destructionOrderOptional.isPresent()) {
            return addDestructionOrder(destructionOrderDTO);
        }

        DestructionOrder destructionOrder = destructionOrderMapper.destructionOrderDtoToDestructionOrder(destructionOrderDTO);
        DestructionOrder savedDestructionOrder = changeFromPreviousDestructionOrderToNew(destructionOrderOptional.get(), destructionOrder);
        return destructionOrderMapper.destructionOrderToDestructionOrderDto(savedDestructionOrder);
    }

    private DestructionOrder saveDestructionOrder(DestructionOrder destructionOrder) {
        checkAndCountToolsAfterDestructionOrder(destructionOrder);

        Employee warehouseman = employeeService.getLoggedEmployee();
        destructionOrder.setWarehouseman(warehouseman);

        return destructionOrderRepository.save(destructionOrder);
    }

    private DestructionOrder changeFromPreviousDestructionOrderToNew(DestructionOrder previousDestructionOrder, DestructionOrder newDestructionOrder) {

        restoreStateBeforeDestructionOrder(previousDestructionOrder);
        checkAndCountToolsAfterDestructionOrder(newDestructionOrder);

        newDestructionOrder.setWarehouseman(employeeService.getLoggedEmployee());
        newDestructionOrder.setId(previousDestructionOrder.getId());
        return destructionOrderRepository.save(newDestructionOrder);
    }

    private void restoreStateBeforeDestructionOrder(DestructionOrder previousDestructionOrder) {
        previousDestructionOrder.getDestructionOrderTools()
                .forEach(destructionOrderTool -> {
                    Tool tool = destructionOrderTool.getTool();
                    undoDestructionChangesOnTool(tool, destructionOrderTool.getCount());
                });
    }

    private void checkAndCountToolsAfterDestructionOrder(DestructionOrder newDestructionOrder) {
        makeBidirectionalRelations(newDestructionOrder);

        newDestructionOrder.getDestructionOrderTools()
                .forEach(destructionOrderTool -> {
                    Tool tool = destructionOrderTool.getTool();
                    checkIfToolIsEnable(tool);
                    long count = destructionOrderTool.getCount();
                    if (tool.getAllCount() < count || tool.getCurrentCount() < count) {
                        throw new IllegalArgumentException("Cannot destroy " + tool.getName() + " tool, because destroy count was bigger then number of tool");
                    }
                    tool.setCurrentCount(tool.getCurrentCount() - count);
                    tool.setAllCount(tool.getAllCount() - count);
                    this.setIsEnableIfToolIsUnique(tool, false);
                }
        );
    }

    private void makeBidirectionalRelations(DestructionOrder destructionOrder) {
        List<DestructionOrderTool> destructionOrderTools = new ArrayList<>(destructionOrder.getDestructionOrderTools());
        destructionOrder.setDestructionOrderTools(new ArrayList<>());
        destructionOrderTools.forEach(destructionOrderTool -> {
            long toolId = destructionOrderTool.getTool().getId();
            Tool tool = toolService.findToolById(toolId);
            tool.addDestructionOrderTool(destructionOrderTool);
            destructionOrder.addDestroyedTool(destructionOrderTool);
        });
    }

    private void checkIfToolIsEnable(Tool tool) {
        if (!tool.getIsEnable()) {
            throw new IllegalArgumentException("Cannot destroy " + tool.getName() + " tool, because it was disabled");
        }
    }

    private void setIsEnableIfToolIsUnique(Tool tool, boolean isEnable) {
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
