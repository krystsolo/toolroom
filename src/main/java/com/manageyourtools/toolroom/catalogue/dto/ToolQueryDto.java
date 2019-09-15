package com.manageyourtools.toolroom.catalogue.dto;

import com.manageyourtools.toolroom.catalogue.domain.UnitOfMeasure;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Builder
@Getter
public class ToolQueryDto {

    private Long id;
    @NotNull
    @NotBlank
    private String name;
    private Long currentCount;

    private UnitOfMeasure unitOfMeasure;
    private Long allCount;

    private CategoryDto category;
    private Long minimalCount;
    @NotNull
    private Boolean isUnique;
    @NotNull
    private Boolean isToReturn;
    private LocalDate warrantyDate;
    private String location;
    @NotNull
    private Boolean isEnable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToolQueryDto that = (ToolQueryDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
