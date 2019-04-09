package com.manageyourtools.toolroom.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LendingReturnOrderToolDTO {
    private Long id;

    private LocalDateTime returnDate;

    private Boolean isReturned;

    private Long returnWarehousemanId;

    private ToolDTO tool;

    @Min(1)
    private int count;
}
