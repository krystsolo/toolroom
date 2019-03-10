package com.manageyourtools.toolroom.domains;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Employee {

    private static final String ROLE_PREFIX = "ROLE_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    @Size(min = 3, max = 30)
    @Column(unique = true)
    private String userName;

    @NotNull
    private String surName;

    @Lob
    private byte[] image;


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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Email
    private String email;

    @OneToMany(mappedBy = "warehouseman", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
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

    public Collection<? extends GrantedAuthority> getAuthoritiesFromRoles(){
        return this.roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.getRoleType().name()))
                .collect(Collectors.toList());
    }
}
