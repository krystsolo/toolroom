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
public class Destroyed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp addTimestamp;

    @UpdateTimestamp
    private Timestamp editTimestamp;

    @OneToMany(mappedBy = "destroyed")
    private List<DestroyedTool> destroyedTools = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "warehouseman_id")
    private Employee warehouseman;

    private String description;

    public Destroyed addDestroyedTool(DestroyedTool destroyedTool){
        destroyedTool.setDestroyed(this);
        this.destroyedTools.add(destroyedTool);
        return this;
    }
}
