package com.manageyourtools.toolroom.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @Fetch(value = FetchMode.SUBSELECT)
    private List<LendingOrderTool> lendingOrderTools = new ArrayList<>();

    private LocalDate returnUntilTime;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "warehouseman_id")
    private Employee warehouseman;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "worker_id")
    private Employee worker;

    @NotNull
    @Column(unique = true)
    private String orderNumber;

    @UpdateTimestamp
    private Timestamp editTime;

    @OneToOne(cascade = CascadeType.ALL)
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
