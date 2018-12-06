package com.manageyourtools.toolroom.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class DestructionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp addTimestamp;

    @UpdateTimestamp
    private Timestamp editTimestamp;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "destructionOrder")
    @JsonIgnore
    private List<DestructionOrderTool> destructionOrderTools = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "warehouseman_id")
    private Employee warehouseman;

    private String description;

    public DestructionOrder addDestroyedTool(DestructionOrderTool destructionOrderTool){
        destructionOrderTool.setDestructionOrder(this);
        this.destructionOrderTools.add(destructionOrderTool);
        return this;
    }
}
