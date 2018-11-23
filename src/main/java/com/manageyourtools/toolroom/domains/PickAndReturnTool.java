package com.manageyourtools.toolroom.domains;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class PickAndReturnTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp returnDate;

    private Boolean isReturned;

    @ManyToOne
    @JoinColumn(name = "returnWarehouseman_id")
    private Employee returnWarehouseman;

    @ManyToOne
    @JoinColumn(name = "pickAndReturn_id")
    private PickAndReturn pickAndReturn;

    @ManyToOne
    @JoinColumn(name = "tool_id")
    private Tool tool;
}
