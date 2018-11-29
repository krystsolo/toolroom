package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.domains.Role;
import com.manageyourtools.toolroom.repositories.EmployeeRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    public static final String HAS_ROLE_WAREHOUSEMAN = "hasRole('WAREHOUSEMAN')";
    public static final String HAS_ROLE_ADMIN = "hasRole('ADMIN')";
    private static final String ROLE_PREFIX = "ROLE_";

    private final EmployeeRepository employeeRepository;

    public UserDetailsServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByUserName(username);

        if(employee == null || !employee.getIsActive()) {
            //todo implement better exception handling
           throw new RuntimeException("employee with username: " + username + " not found");
        }

        System.out.println(getAuthorities(employee.getRoles()));
        return new User(
                employee.getUserName(),
                employee.getPassword(),
                getAuthorities(employee.getRoles()));

    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles){
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.getRoleType().name()))
                .collect(Collectors.toList());
    }
}
