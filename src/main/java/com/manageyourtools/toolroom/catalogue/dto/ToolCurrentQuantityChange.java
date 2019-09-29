package com.manageyourtools.toolroom.catalogue.dto;

import lombok.Value;

@Value
public class ToolCurrentQuantityChange {
    long toolId;
    long quantityChange;
}
