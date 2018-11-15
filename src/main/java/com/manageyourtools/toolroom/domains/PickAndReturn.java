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
    private List<PickAndReturnTool> pickAndReturn = new ArrayList<>();

    private Boolean isToReturn;

    private Timestamp returnUntilTime;

    @OneToOne
    private Employee warehouseman;

    @OneToOne
    private Employee worker;

    private String orderNumber;

    private Boolean isEdited;

    @UpdateTimestamp
    private Timestamp editTime;

    private String description;

    private Timestamp returnTime;
}
