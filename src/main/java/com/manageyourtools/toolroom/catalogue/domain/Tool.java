package com.manageyourtools.toolroom.catalogue.domain;

import com.manageyourtools.toolroom.catalogue.dto.ToolDto;
import com.manageyourtools.toolroom.catalogue.dto.ToolQueryDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
@Builder
class Tool {

    public static final Long INITIAL_QUANTITY = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    private Long currentCount;

    @Enumerated(value = EnumType.STRING)
    private UnitOfMeasure unitOfMeasure;

    @Column(name = "all_count")
    private Long allCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "minimal_count")
    private Long minimalCount;

    @NotNull
    private Boolean isUnique;

    @NotNull
    private Boolean isToReturn;
    private LocalDate warrantyDate;
    private String location;

    @NotNull
    private Boolean isEnable = true;

    ToolDto toDto() {
        return ToolDto.builder()
                .id(id)
                .name(name)
                .category(category.toDto())
                .unitOfMeasure(unitOfMeasure)
                .minimalCount(minimalCount)
                .isUnique(isUnique)
                .isToReturn(isToReturn)
                .warrantyDate(warrantyDate)
                .location(location)
                .build();
    }

    ToolQueryDto toQuery() {
        return ToolQueryDto.builder()
                .id(id)
                .name(name)
                .category(category.toDto())
                .unitOfMeasure(unitOfMeasure)
                .allCount(allCount)
                .currentCount(currentCount)
                .isEnable(isEnable)
                .minimalCount(minimalCount)
                .isUnique(isUnique)
                .isToReturn(isToReturn)
                .warrantyDate(warrantyDate)
                .location(location)
                .build();
    }

    void disable() {
        if (allCount.equals(INITIAL_QUANTITY)) {
            isEnable = false;
            warrantyDate = null;
            minimalCount = null;

        } else {
            throw new IllegalArgumentException("Tool can not be disabled, because there are still " + allCount + " tools in the warehouse.");
        }
    }

    void changeCategory(Category category) {
        this.category = category;
    }

    void changeAllQuantity(long quantityChange) {
        if (!isEnable) {
            throw new IllegalStateException("Cannot change tool all quantity because the tool is disabled");
        }

        allCount =+ quantityChange;
        currentCount =+ quantityChange;
        if (toolCountOnWarehouseIsBelowZero()) {
            throw new IllegalStateException("Tool quantity cannot be below zero");
        }
    }

    private boolean toolCountOnWarehouseIsBelowZero() {
        return allCount < 0 || currentCount < 0;
    }

    void changeCurrentQuantity(long quantityChange) {
        if (!isEnable) {
            throw new IllegalStateException("Cannot change tool all quantity because the tool is disabled");
        }

        currentCount =+ quantityChange;
        if (toolCountOnWarehouseIsBelowZero() || allCount < currentCount) {
            throw new IllegalStateException("Illegal tool quantity state");
        }
    }
}
