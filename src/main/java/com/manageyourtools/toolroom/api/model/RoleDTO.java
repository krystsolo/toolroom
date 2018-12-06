package com.manageyourtools.toolroom.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Long id;
    @NotNull
    private String roleType;
}
