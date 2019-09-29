package com.manageyourtools.toolroom.warehouse.domain;

import com.manageyourtools.toolroom.warehouse.dto.OrderDto;
import com.manageyourtools.toolroom.warehouse.dto.OrderQueryDto;
import com.manageyourtools.toolroom.warehouse.dto.OrderToolDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity(name = "Order")
@Builder
@NoArgsConstructor
@EqualsAndHashCode(of = "orderCode")
class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private UUID orderCode;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    private LocalDateTime addTimestamp;

    private LocalDateTime editTimestamp;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "buyOrder")
    private Set<OrderTool> orderTools = new HashSet<>();

    private Long warehousemanId;

    private String description;

    OrderType getType(){
        return type;
    }

    OrderDto toDto() {
        return OrderDto.builder()
                .id(id)
                .description(description)
                .orderTools(toToolsDto())
                .build();
    }

    OrderQueryDto toQuery() {
        return OrderQueryDto.builder()
                .id(id)
                .orderCode(orderCode)
                .type(type)
                .description(description)
                .addTimestamp(addTimestamp)
                .editTimestamp(editTimestamp)
                .warehousemanId(warehousemanId)
                .orderTools(toToolsDto())
                .build();
    }

    private Set<OrderToolDto> toToolsDto() {
        return orderTools.stream().map(OrderTool::toDto).collect(Collectors.toSet());
    }
}
