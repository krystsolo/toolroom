package com.manageyourtools.toolroom.domains;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String firstName;
    @NotNull
    private String surnameName;

    @Lob
    private String image;

    @NotNull
    private Role role;

    @NotNull
    private Boolean isActive;

    private LocalDate hireDate;

    //make it as next table
    private String workingGroup;

    @Size(min = 9, max = 9)
    private Long phoneNumber;

    private String password;

    @Email
    private String email;


}
