package com.manageyourtools.toolroom.catalogue.dto;

import lombok.Value;

@Value
public class ToolCurrentQuantityChange {
    Long toolId;
    long quantityChange;
}
