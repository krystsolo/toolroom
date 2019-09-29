package com.manageyourtools.toolroom.warehouse.dto;

import com.manageyourtools.toolroom.warehouse.domain.OrderType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
public class OrderQueryDto {
    private Long id;
    private UUID orderCode;
    private OrderType type;
    private LocalDateTime addTimestamp;
    private LocalDateTime editTimestamp;
    private String description;
    private Long warehousemanId;
    private Set<OrderToolDto> orderTools;
}
