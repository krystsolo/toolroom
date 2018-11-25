package com.manageyourtools.toolroom.domains;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class DestructionOrderTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long count;

    private String description;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "destructionOrder_id")
    private DestructionOrder destructionOrder;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "tool_id")
    private Tool tool;
}
