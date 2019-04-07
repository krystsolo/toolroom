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

        List<DestructionOrderTool> destructionOrderTools = new ArrayList<>(destructionOrder.getDestructionOrderTools());
        destructionOrderTools.forEach(destructionOrderTool -> {
            long toolId = destructionOrderTool.getTool().getId();
            Tool tool = toolService.findToolById(toolId);
            if (!tool.getIsEnable()) {
                throw new IllegalArgumentException("Cannot destroy " + tool.getName() + " tool, because it was disabled");
            }
            long count = destructionOrderTool.getCount();
            if (tool.getAllCount() < count || tool.getCurrentCount() < count) {
                throw new IllegalArgumentException("Cannot destroy " + tool.getName() + " tool, because destroy count was bigger then number of tool");
            }
            tool.setCurrentCount(tool.getCurrentCount() - count);
            tool.setAllCount(tool.getAllCount() - count);
            if (tool.getIsUnique()) {
                tool.setIsEnable(false);
            }

            tool.addDestructionOrderTool(destructionOrderTool);
            destructionOrder.addDestroyedTool(destructionOrderTool);
        });

        Employee warehouseman = employeeService.getEmployeeByUsername(
                authenticationFacade.getUsernameOfCurrentLoggedUser());
        destructionOrder.setWarehouseman(warehouseman);

        DestructionOrder savedDestructionOrder = destructionOrderRepository.save(destructionOrder);
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

        List<DestructionOrderTool> destructionOrderTools = new ArrayList<>(destructionOrder.getDestructionOrderTools());

        List<DestructionOrderTool> previousDestructionOrderTools = destructionOrderOptional.get().getDestructionOrderTools();

        previousDestructionOrderTools.stream()
                .filter(destructionOrderTool -> !isAnyDestructionOrderToolForTool(destructionOrderTools, destructionOrderTool.getTool()))
                .forEach(destructionOrderTool -> {
                    Tool tool = destructionOrderTool.getTool();
                    undoDestructionChangesOnTool(tool, destructionOrderTool.getCount());
                });

        destructionOrderTools.forEach(destructionOrderTool -> {
            long toolId = destructionOrderTool.getTool().getId();
            Tool tool = toolService.findToolById(toolId);

            this.discardPreviousDestructionChangesOnToolIfPresent(previousDestructionOrderTools, tool);
            long count = destructionOrderTool.getCount();
            if (tool.getAllCount() < count || tool.getCurrentCount() < count) {
                throw new IllegalArgumentException("Cannot destroy " + tool.getName() + " tool, because destroy count was bigger then number of tool");
            }
            tool.setCurrentCount(tool.getCurrentCount() - count);
            tool.setAllCount(tool.getAllCount() - count);
            this.setIsEnableIfToolIsUnique(tool, false);

            tool.addDestructionOrderTool(destructionOrderTool);
            destructionOrder.addDestroyedTool(destructionOrderTool);

        });

        Employee warehouseman = employeeService.getEmployeeByUsername(
                authenticationFacade.getUsernameOfCurrentLoggedUser());
        destructionOrder.setWarehouseman(warehouseman);
        destructionOrder.setId(id);
        DestructionOrder savedDestructionOrder = destructionOrderRepository.save(destructionOrder);
        return destructionOrderMapper.destructionOrderToDestructionOrderDto(savedDestructionOrder);
    }

    private void setIsEnableIfToolIsUnique(Tool tool, boolean isEnable) {
        if (tool.getIsUnique()) {
            tool.setIsEnable(isEnable);
        }
    }

    private boolean isAnyDestructionOrderToolForTool(List<DestructionOrderTool> destructionOrderTools, Tool tool) {
        return destructionOrderTools.stream().anyMatch(destructionOrderTool -> destructionOrderTool.getTool().getId().equals(tool.getId()));
    }

    protected void discardPreviousDestructionChangesOnToolIfPresent(List<DestructionOrderTool> destructionOrderTools, Tool tool) {
        destructionOrderTools.stream()
                .filter(destructionOrderTool -> destructionOrderTool.getTool().getId().equals(tool.getId()))
                .findFirst()
                .ifPresent(destructionOrderTool -> undoDestructionChangesOnTool(tool, destructionOrderTool.getCount()));
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
