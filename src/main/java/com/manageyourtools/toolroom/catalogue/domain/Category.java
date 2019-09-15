package com.manageyourtools.toolroom.catalogue.domain;

import com.manageyourtools.toolroom.catalogue.dto.CategoryDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    CategoryDto toDto() {
        return new CategoryDto(id, name);
    }
}
