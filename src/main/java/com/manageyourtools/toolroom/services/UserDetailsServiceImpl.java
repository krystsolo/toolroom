package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.EmployeeRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    static final String HAS_ROLE_WAREHOUSEMAN = "hasRole('WAREHOUSEMAN')";
    static final String HAS_ROLE_ADMIN = "hasRole('ADMIN')";

    private final EmployeeRepository employeeRepository;

    public UserDetailsServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with username=" +  username + " not found"));

        if(!employee.getIsActive()) {
           throw new IllegalArgumentException("employee with username: " + username + "is inactive");
        }

        return new User(
                employee.getUserName(),
                employee.getPassword(),
                employee.getAuthoritiesFromRoles());

    }


}
