package com.manageyourtools.toolroom.domains;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class DestroyedTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long count;

    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pickAndReturn_id")
    private PickAndReturn pickAndReturn;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tool_id")
    private Tool tool;
}
