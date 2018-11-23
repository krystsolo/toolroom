package com.manageyourtools.toolroom.domains;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private RoleEnum roleType;

    //@ManyToMany(mappedBy = "roles")
    //private Set<Employee> employees;
}
