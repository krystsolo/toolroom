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
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "orderNumber")
public class LendingReturnOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp pickTime;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "lendingReturnOrder", orphanRemoval = true)
    //@Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private Set<LendingReturnOrderTool> lendingReturnOrderTools = new HashSet<>();

    private LocalDate returnUntilTime;

    private Boolean isReturned = false;

    @ManyToOne
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

    @ManyToOne
    @JoinColumn(name = "returnWarehouseman_id")
    private Employee returnWarehouseman;

    public void addLendingReturnOrderTool(LendingReturnOrderTool lendingReturnOrderTool){
        lendingReturnOrderTool.setLendingReturnOrder(this);
        this.lendingReturnOrderTools.add(lendingReturnOrderTool);
    }
}
