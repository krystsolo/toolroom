package com.manageyourtools.toolroom.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolDTO {

    private Long id;

    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private Long currentCount;

    private String unitOfMeasure;
    @NotNull
    private Long allCount;

    private CategoryDTO category;

    private Long minimalCount;

    @NotNull
    private Boolean isUnique;

    @NotNull
    private Boolean isToReturn;
    private LocalDate warrantyDate;
    private String location;

    @NotNull
    private Boolean isEnable;

}
