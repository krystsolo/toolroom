package com.manageyourtools.toolroom.warehouse.domain;

import com.manageyourtools.toolroom.warehouse.dto.OrderToolDto;
import com.manageyourtools.toolroom.warehouse.dto.OrderDto;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
class OrderFactory {

    private final OrderRepository orderRepository;

    Order from(OrderDto orderDto, OrderType orderType, Long warehousemanId) {
        Order.OrderBuilder builder = Order.builder()
                .id(orderDto.getId())
                .description(orderDto.getDescription())
                .editTimestamp(LocalDateTime.now())
                .orderTools(from(orderDto.getOrderTools()))
                .warehousemanId(warehousemanId)
                .type(orderType);
        if (orderDto.getId() != null) {
            Order order = orderRepository.getOrderOrThrow(orderDto.getId());
            builder.addTimestamp(order.toQuery().getAddTimestamp())
                    .orderCode(order.toQuery().getOrderCode());

        } else {
            builder.orderCode(UUID.randomUUID())
                    .addTimestamp(LocalDateTime.now());
        }

        return builder.build();
    }

    private Set<OrderTool> from(Set<OrderToolDto> buyOrderTools) {
        if (isMoreOrdersForTool(buyOrderTools)) {
            throw new IllegalArgumentException("There cannot be more than one position on order for tool");
        }
        return buyOrderTools.stream()
                .map(buyOrderToolDto ->
                        OrderTool.builder()
                                .id(buyOrderToolDto.getId())
                                .count(buyOrderToolDto.getCount())
                                .toolId(buyOrderToolDto.getToolId())
                                .build())
                .collect(Collectors.toSet());
    }

    private boolean isMoreOrdersForTool(Set<OrderToolDto> buyOrderTools) {
        return buyOrderTools.size() == buyOrderTools.stream()
                .map(OrderToolDto::getToolId)
                .collect(Collectors.toSet())
                .size();
    }
}
