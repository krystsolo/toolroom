package com.manageyourtools.toolroom.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LendingOrderToolDTO {

    private Long id;

    private ToolDTO tool;

    @Min(1)
    private int count;
}
