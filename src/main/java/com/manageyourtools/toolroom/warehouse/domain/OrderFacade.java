package com.manageyourtools.toolroom.warehouse.domain;

import com.manageyourtools.toolroom.catalogue.domain.CatalogueFacade;
import com.manageyourtools.toolroom.catalogue.dto.ToolAllQuantityChange;
import com.manageyourtools.toolroom.employee.dto.EmployeeDTO;
import com.manageyourtools.toolroom.infrastructure.config.AuthenticationFacade;
import com.manageyourtools.toolroom.warehouse.dto.OrderToolDto;
import com.manageyourtools.toolroom.warehouse.dto.OrderDto;
import com.manageyourtools.toolroom.warehouse.dto.OrderQueryDto;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@AllArgsConstructor
public class OrderFacade {
    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;
    private final AuthenticationFacade authenticationFacade;
    private final CatalogueFacade catalogueFacade;

    public OrderQueryDto find(Long id) {
        return orderRepository.getOrderOrThrow(id).toQuery();
    }

    public Set<OrderQueryDto> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(Order::toQuery)
                .collect(Collectors.toSet());
    }

    public OrderQueryDto addOrder(OrderDto orderDto, OrderType orderType) {
        EmployeeDTO employeeDTO = authenticationFacade.getLoggedUser();
        Order order = orderFactory.from(orderDto, orderType, employeeDTO.getId());
        if (order.getType() == OrderType.SUPPLY) {
            increaseToolsCount(orderDto.getOrderTools());
        } else {
            decreaseToolsCount(orderDto.getOrderTools());
        }
        return order.toQuery();

    }

    public void delete(Long id) {
        orderRepository.findById(id)
                .ifPresent(order -> {
                    if (order.getType() == OrderType.SUPPLY) {
                        decreaseToolsCount(order.toDto().getOrderTools());
                    } else {
                        increaseToolsCount(order.toDto().getOrderTools());
                    }
                    orderRepository.delete(id);
                });
    }

    public OrderQueryDto update(OrderDto orderDto) {
        EmployeeDTO employeeDTO = authenticationFacade.getLoggedUser();
        OrderQueryDto savedOrderDto = orderRepository.getOrderOrThrow(orderDto.getId()).toQuery();
        Order order = orderFactory.from(orderDto, savedOrderDto.getType(), employeeDTO.getId());
        order = orderRepository.save(order);
        handleToolQuantityAdditionDifferences(orderDto.getOrderTools(), savedOrderDto.getOrderTools(), order.getType());
        return order.toQuery();
    }

    private void increaseToolsCount(Set<OrderToolDto> buyOrderTools) {
        buyOrderTools.forEach(buyOrderToolDto ->
                catalogueFacade.calculateToolAllQuantity(
                        new ToolAllQuantityChange(buyOrderToolDto.getToolId(), buyOrderToolDto.getCount()))
        );
    }

    private void decreaseToolsCount(Set<OrderToolDto> buyOrderTools) {
        buyOrderTools.forEach(buyOrderToolDto ->
                catalogueFacade.calculateToolAllQuantity(
                        new ToolAllQuantityChange(buyOrderToolDto.getToolId(), buyOrderToolDto.getCount()))
        );
    }

    private void handleToolQuantityAdditionDifferences(Set<OrderToolDto> update, Set<OrderToolDto> saved, OrderType orderType) {
        Map<Long, Long> updateToolCountChange = toToolCountChangeMap(update);
        Map<Long, Long> savedToolCountChange = toToolCountChangeMap(saved);
        Map<Long, Long> collect = calculateToolCountChange(updateToolCountChange, savedToolCountChange);
        collect.keySet().removeIf(change -> change.equals(0L));
        collect.forEach((toolId, quantity) ->
                catalogueFacade.calculateToolAllQuantity(
                        new ToolAllQuantityChange(
                                toolId,
                                orderType == OrderType.SUPPLY ? quantity : -1 * quantity)));
    }

    private Map<Long, Long> calculateToolCountChange(Map<Long, Long> updateToolCountChange, Map<Long, Long> savedToolCountChange) {
        return Stream.of(updateToolCountChange, savedToolCountChange)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value1, value2) -> value1 - value2));
    }

    private Map<Long, Long> toToolCountChangeMap(Set<OrderToolDto> orderTools) {
        return orderTools.stream()
                .collect(Collectors.toMap(
                        OrderToolDto::getToolId,
                        OrderToolDto::getCount));
    }


}
