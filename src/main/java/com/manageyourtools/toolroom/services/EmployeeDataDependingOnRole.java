package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.config.AuthenticationFacade;
import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.domains.RoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class EmployeeDataDependingOnRole {

    private final AuthenticationFacade authenticationFacade;

    public EmployeeDataDependingOnRole(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    public Employee getEmployeeDataRelevantForAuthorisation(Employee employee){

        Collection<? extends GrantedAuthority> authorities = authenticationFacade.getAuthentication().getAuthorities();

        SimpleGrantedAuthority admin = new SimpleGrantedAuthority(RoleEnum.ADMIN.name());

        if(!authorities.contains(admin)){
            employee.setPassword(null);
            employee.setRoles(null);
        }
        return employee;
    }
}
