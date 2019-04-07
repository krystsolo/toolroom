package com.manageyourtools.toolroom.api.model;

import com.manageyourtools.toolroom.domains.DestructionOrderTool;
import com.manageyourtools.toolroom.domains.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestructionOrderDTO {

    private Long id;

    @NotNull
    private String orderCode;

    private Timestamp addTimestamp;

    private Timestamp editTimestamp;

    private List<DestructionOrderToolDTO> destructionOrderTools = new ArrayList<>();

    private Long warehousemanId;

    private String description;
}
