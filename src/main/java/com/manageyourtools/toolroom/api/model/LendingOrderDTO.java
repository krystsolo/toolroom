package com.manageyourtools.toolroom.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LendingOrderDTO {

    private Long id;

    private Timestamp pickTime;

    private List<LendingOrderToolDTO> lendingOrderTools = new ArrayList<>();

    private LocalDate returnUntilTime;

    private Long warehousemanId;

    private Long workerId;

    @NotNull
    private String orderNumber;

    private Timestamp editTime;

    private String description;

    private Long lendingReturnOrderId;
}
