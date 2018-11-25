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
public class BuyOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp boughtTimestamp;

    @UpdateTimestamp
    private Timestamp editTimestamp;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "buyOrder")
    private List<BuyOrderTool> buyOrderTools = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "warehouseman_id")
    private Employee warehouseman;

    private String description;

    public BuyOrder addBoughtTool(BuyOrderTool buyOrderTool){
        buyOrderTool.setBuyOrder(this);
        this.buyOrderTools.add(buyOrderTool);
        return this;
    }
}
