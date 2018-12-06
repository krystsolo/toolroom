package com.manageyourtools.toolroom.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyOrderToolDTO {

    private Long id;

    private Long toolId;

    @Min(1)
    private Long count;
}
