package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.config.JwtTokenUtil;
import com.manageyourtools.toolroom.domains.AuthToken;
import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.domains.LoginUser;
import com.manageyourtools.toolroom.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final EmployeeService employeeService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, EmployeeService employeeService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.employeeService = employeeService;
    }

    @PostMapping(value = "/generate-token")
    @ResponseStatus(HttpStatus.OK)
    public AuthToken login(@RequestBody LoginUser loginUser) throws AuthenticationException {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
        final Employee employee = employeeService.getEmployeeByUsername(loginUser.getUsername());
        final String token = jwtTokenUtil.generateToken(employee);
        return new AuthToken(token, employee.getUserName());
    }

    @PostMapping(value = "/user/logout")
    @ResponseStatus(HttpStatus.OK)
    public String logout(){
        SecurityContextHolder.clearContext();

        return "Logout successfully!";
    }

}