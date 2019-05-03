package com.manageyourtools.toolroom.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "orderCode")
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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "buyOrder")
    @JsonIgnore
    private Set<BuyOrderTool> buyOrderTools = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "warehouseman_id")
    private Employee warehouseman;

    private String description;

    public BuyOrder addBuyOrderTool(BuyOrderTool buyOrderTool) {
        this.buyOrderTools.add(buyOrderTool);
        buyOrderTool.setBuyOrder(this);
        return this;
    }
}
