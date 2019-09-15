package com.manageyourtools.toolroom.catalogue.dto;

import com.manageyourtools.toolroom.catalogue.domain.UnitOfMeasure;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@Getter
public class ToolDto {

    private Long id;
    @NotNull
    @NotBlank
    private String name;
    private UnitOfMeasure unitOfMeasure;
    private CategoryDto category;
    private Long minimalCount;
    @NotNull
    private Boolean isUnique;
    @NotNull
    private Boolean isToReturn;
    private LocalDate warrantyDate;
    private String location;

}
