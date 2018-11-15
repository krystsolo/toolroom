package com.manageyourtools.toolroom.domains;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Data
public class BoughtTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tool_id")
    private Tool tool;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bought_id")
    private Bought bought;

    @Min(1)
    private Long count;
}
