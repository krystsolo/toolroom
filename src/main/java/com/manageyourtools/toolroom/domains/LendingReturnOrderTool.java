package com.manageyourtools.toolroom.domains;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class LendingReturnOrderTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime returnDate;

    private Boolean isReturned = false;

    @ManyToOne
    @JoinColumn(name = "returnWarehouseman_id")
    private Employee returnWarehouseman;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "lendingReturnOrder_id")
    private LendingReturnOrder lendingReturnOrder;

    @ManyToOne
    @JoinColumn(name = "tool_id")
    private Tool tool;

    @Min(1)
    private int count;

}
