package com.manageyourtools.toolroom.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class DestructionOrder {

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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "destructionOrder", orphanRemoval = true)
    @JsonIgnore
    private Set<DestructionOrderTool> destructionOrderTools = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "warehouseman_id")
    private Employee warehouseman;

    private String description;

    public DestructionOrder addDestroyedTool(DestructionOrderTool destructionOrderTool){
        this.destructionOrderTools.add(destructionOrderTool);
        destructionOrderTool.setDestructionOrder(this);
        return this;
    }
}
