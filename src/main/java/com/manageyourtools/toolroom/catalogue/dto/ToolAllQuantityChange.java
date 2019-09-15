package com.manageyourtools.toolroom.catalogue.dto;

import lombok.Value;

@Value
public class ToolAllQuantityChange {
    Long toolId;
    long quantityChange;
}
