package com.manageyourtools.toolroom.domains;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class DestructionOrderTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    private Long count;

    private String description;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "destructionOrder_id")
    private DestructionOrder destructionOrder;

    @ManyToOne
    @JoinColumn(name = "tool_id")
    private Tool tool;
}
