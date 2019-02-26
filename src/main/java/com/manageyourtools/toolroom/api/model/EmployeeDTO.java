package com.manageyourtools.toolroom.api.model;

import com.manageyourtools.toolroom.domains.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private Long id;
    @NotNull
    private String firstName;

    @NotNull
    @Min(2)
    private String userName;
    @NotNull
    private String surName;

    private Set<RoleDTO> roles = new HashSet<>();

    @NotNull
    private Boolean isActive;

    private String workingGroup;

    private Long phoneNumber;
    @Min(2)
    private String password;

    @Email
    private String email;

}
