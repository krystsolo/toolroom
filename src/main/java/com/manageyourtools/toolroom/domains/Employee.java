package com.manageyourtools.toolroom.domains;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
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

    private String workingGroup;

    private Long phoneNumber;

    @NotNull
    private String password;

    @Email
    private String email;
    
    @OneToMany(mappedBy = "warehouseman")
    @JsonIgnore
    private List<BuyOrder> buyOrders;

    @OneToMany(mappedBy = "warehouseman")
    @JsonIgnore
    private List<DestructionOrder> destructionOrders;

    @OneToMany(mappedBy = "pickWarehouseman")
    @JsonIgnore
    private List<LendingOrder> lendingOrdersRentals;

    @OneToMany(mappedBy = "worker")
    @JsonIgnore
    private List<LendingOrder> lendingOrdersDownloads;

    @OneToMany(mappedBy = "returnWarehouseman")
    @JsonIgnore
    private List<LendingOrder> lendingOrdersReturns;

    @OneToMany(mappedBy = "returnWarehouseman")
    @JsonIgnore
    private List<LendingOrder> lendingOrderToolReturns;
}
