package com.manageyourtools.toolroom.warehouse.domain;

import com.manageyourtools.toolroom.warehouse.dto.OrderToolDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@NoArgsConstructor
@Getter
@EqualsAndHashCode(exclude = "id")
class OrderTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long toolId;
    private String description;

    @Min(1)
    private Long count;

    OrderToolDto toDto() {
        return OrderToolDto.builder()
                .id(id)
                .count(count)
                .toolId(toolId)
                .build();
    }
}
