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
public class Bought {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp boughtTimestamp;

    @UpdateTimestamp
    private Timestamp editTimestamp;

    @OneToMany(mappedBy = "bought")
    private List<BoughtTool> boughtToolList = new ArrayList<>();

    @OneToOne
    private Employee warehouseman;

    private String description;


}
