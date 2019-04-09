package com.manageyourtools.toolroom.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LendingReturnOrderDTO {

    private Long id;

    private Timestamp pickTime;

    private List<LendingReturnOrderToolDTO> lendingReturnOrderTools = new ArrayList<>();

    private LocalDate returnUntilTime;

    private Boolean isReturned;

    private Long workerId;

    @NotNull
    private String orderNumber;

    private Timestamp editTime;

    private String description;

    private LocalDateTime returnTime;

    private Long returnWarehousemanId;

    @NotNull
    private Long lendingOrderId;
}
