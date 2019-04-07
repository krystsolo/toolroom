package com.manageyourtools.toolroom.api.model;

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
public class BuyOrderDTO {
    private Long id;

    @NotNull
    private String orderCode;

    private Timestamp addTimestamp;

    private Timestamp editTimestamp;

    private List<BuyOrderToolDTO> buyOrderTools = new ArrayList<>();

    private Long warehousemanId;

    private String description;
}
