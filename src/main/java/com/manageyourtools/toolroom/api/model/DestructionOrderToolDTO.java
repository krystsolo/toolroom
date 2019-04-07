package com.manageyourtools.toolroom.api.model;

import com.manageyourtools.toolroom.domains.DestructionOrder;
import com.manageyourtools.toolroom.domains.Tool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestructionOrderToolDTO {

    private Long id;

    @Min(1)
    private Long count;

    private String description;

    private ToolDTO tool;
}
