package com.manageyourtools.toolroom.domains;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    @Column(unique = true)
    private String userName;

    @NotNull
    private String surName;

    @Lob
    private String image;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employee_role",
            joinColumns = @JoinColumn(name = "employee_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName="id"))
    private Set<Role> roles = new HashSet<>();

    @NotNull
    private Boolean isActive;

    //make it as next table
    private String workingGroup;


    private Long phoneNumber;

    @NotNull
    private String password;

    @Email
    private String email;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

}
