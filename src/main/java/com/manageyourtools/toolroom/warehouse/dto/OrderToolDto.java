package com.manageyourtools.toolroom.warehouse.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder
@Getter
public class OrderToolDto {
    private Long id;
    @NotNull
    private Long toolId;
    private String description;
    @Min(1)
    private Long count;
}
