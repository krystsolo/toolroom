package com.manageyourtools.toolroom.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@EqualsAndHashCode(of = "orderNumber")
public class LendingReturnOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp pickTime;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "lendingReturnOrder", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<LendingReturnOrderTool> lendingReturnOrderTools = new ArrayList<>();

    private LocalDate returnUntilTime;

    private Boolean isReturned = false;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "worker_id")
    private Employee worker;

    @NotNull
    @Column(unique = true)
    private String orderNumber;

    @UpdateTimestamp
    private Timestamp editTime;

    private String description;

    private LocalDateTime returnTime;

    @OneToOne(cascade = {CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH})
    private LendingOrder lendingOrder;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "returnWarehouseman_id")
    private Employee returnWarehouseman;

    public void addLendingReturnOrderTool(LendingReturnOrderTool lendingReturnOrderTool){
        lendingReturnOrderTool.setLendingReturnOrder(this);
        this.lendingReturnOrderTools.add(lendingReturnOrderTool);
    }

    public void removeLendingReturnOrderTool(LendingReturnOrderTool lendingReturnOrderTool) {
        lendingReturnOrderTool.setLendingReturnOrder(null);
        this.lendingReturnOrderTools.remove(lendingReturnOrderTool);
    }
}
