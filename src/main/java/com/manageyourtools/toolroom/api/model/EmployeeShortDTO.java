package com.manageyourtools.toolroom.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeShortDTO {

    private Long id;
    @NotNull
    private String firstName;

    @NotNull
    @Min(2)
    private String userName;
    @NotNull
    private String surName;

    @NotNull
    private Boolean isActive;

    private String workingGroup;

    private Long phoneNumber;

    @Email
    private String email;
}
