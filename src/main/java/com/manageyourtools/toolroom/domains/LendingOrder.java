package com.manageyourtools.toolroom.domains;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class LendingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp pickTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lendingOrder")
    private List<LendingOrderTool> lendingOrderTools = new ArrayList<>();

    private Boolean isToReturn;

    private Timestamp returnUntilTime;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "pickWarehouseman_id")
    private Employee pickWarehouseman;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "worker_id")
    private Employee worker;

    private String orderNumber;

    private Boolean isEdited;

    @UpdateTimestamp
    private Timestamp editTime;

    private String description;

    private Timestamp returnTime;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "returnWarehouseman_id")
    private Employee returnWarehouseman;

    public LendingOrder addPickAndReturnTool(LendingOrderTool lendingOrderTool){
        lendingOrderTool.setLendingOrder(this);
        this.lendingOrderTools.add(lendingOrderTool);
        return this;
    }
}
