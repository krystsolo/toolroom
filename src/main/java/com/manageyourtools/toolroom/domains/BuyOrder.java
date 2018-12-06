package com.manageyourtools.toolroom.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class BuyOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String orderCode;

    @CreationTimestamp
    private Timestamp addTimestamp;

    @UpdateTimestamp
    private Timestamp editTimestamp;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "buyOrder")
    @JsonIgnore
    private List<BuyOrderTool> buyOrderTools = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "warehouseman_id")
    private Employee warehouseman;

    private String description;

    public BuyOrder addBuyOrderTool(BuyOrderTool buyOrderTool){
        buyOrderTool.setBuyOrder(this);
        this.buyOrderTools.add(buyOrderTool);
        return this;
    }
}
