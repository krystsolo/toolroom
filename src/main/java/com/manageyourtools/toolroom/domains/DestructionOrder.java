package com.manageyourtools.toolroom.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class DestructionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String orderCode;

    @CreationTimestamp
    private Timestamp addTimestamp;

    @UpdateTimestamp
    private Timestamp editTimestamp;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "destructionOrder", orphanRemoval = true)
    @JsonIgnore
    private List<DestructionOrderTool> destructionOrderTools = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "warehouseman_id")
    private Employee warehouseman;

    private String description;

    public DestructionOrder addDestroyedTool(DestructionOrderTool destructionOrderTool){
        this.destructionOrderTools.add(destructionOrderTool);
        destructionOrderTool.setDestructionOrder(this);
        return this;
    }

    public void removeDestructionOrderTool(DestructionOrderTool destructionOrderTool) {
        this.destructionOrderTools.remove(destructionOrderTool);
        destructionOrderTool.setDestructionOrder(null);
    }
}
