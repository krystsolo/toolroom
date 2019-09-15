package com.manageyourtools.toolroom.catalogue.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Value
public class CategoryDto {

    private Long id;
    @NotNull
    private String name;
}
