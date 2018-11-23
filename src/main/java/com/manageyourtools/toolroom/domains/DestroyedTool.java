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

    @ManyToOne
    @JoinColumn(name = "destroyed_id")
    private Destroyed destroyed;

    @ManyToOne
    @JoinColumn(name = "tool_id")
    private Tool tool;
}
