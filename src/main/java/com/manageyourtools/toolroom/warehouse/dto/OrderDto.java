package com.manageyourtools.toolroom.warehouse.dto;

import lombok.*;

import java.util.Set;

@Builder
@Getter
public class OrderDto {
    Long id;
    Set<OrderToolDto> orderTools;
    String description;
}
