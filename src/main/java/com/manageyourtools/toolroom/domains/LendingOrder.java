package com.manageyourtools.toolroom.domains;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "orderNumber")
public class LendingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp pickTime;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "lendingOrder", orphanRemoval = true)
    //@Fetch(value = FetchMode.SUBSELECT)
    private Set<LendingOrderTool> lendingOrderTools = new HashSet<>();

    private LocalDate returnUntilTime;

    @ManyToOne
    @JoinColumn(name = "warehouseman_id")
    private Employee warehouseman;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Employee worker;

    @NotNull
    @Column(unique = true)
    private String orderNumber;

    @UpdateTimestamp
    private Timestamp editTime;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private LendingReturnOrder lendingReturnOrder;

    private String description;

    public LendingOrder addLendingOrderTool(LendingOrderTool lendingOrderTool){
        lendingOrderTool.setLendingOrder(this);
        this.lendingOrderTools.add(lendingOrderTool);
        return this;
    }

    public void addLendingReturnOrder(LendingReturnOrder lendingReturnOrder) {
        this.lendingReturnOrder = lendingReturnOrder;
        lendingReturnOrder.setLendingOrder(this);
    }
}
