package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.repositories.EmployeeRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

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

        return new User(
                employee.getUserName(),
                employee.getPassword(),
                AuthorityUtils.createAuthorityList(employee.getRoles().toString())
        );
    }
}
