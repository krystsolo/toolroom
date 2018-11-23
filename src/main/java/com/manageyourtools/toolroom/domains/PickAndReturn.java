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
public class PickAndReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp pickTime;

    @OneToMany(mappedBy = "pickAndReturn")
    private List<PickAndReturnTool> pickAndReturnTools = new ArrayList<>();

    private Boolean isToReturn;

    private Timestamp returnUntilTime;

    @ManyToOne
    @JoinColumn(name = "pickWarehouseman_id")
    private Employee pickWarehouseman;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Employee worker;

    private String orderNumber;

    private Boolean isEdited;

    @UpdateTimestamp
    private Timestamp editTime;

    private String description;

    private Timestamp returnTime;

    @ManyToOne
    @JoinColumn(name = "returnWarehouseman_id")
    private Employee returnWarehouseman;

    public PickAndReturn addPickAndReturnTool(PickAndReturnTool pickAndReturnTool){
        pickAndReturnTool.setPickAndReturn(this);
        this.pickAndReturnTools.add(pickAndReturnTool);
        return this;
    }
}
