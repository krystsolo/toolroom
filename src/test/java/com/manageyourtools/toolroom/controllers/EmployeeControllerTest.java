package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.services.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerTest {

    @Mock
    EmployeeService employeeService;

    @InjectMocks
    EmployeeController controller;

    MockMvc mockMvc;

    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        bCryptPasswordEncoder = new BCryptPasswordEncoder();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    private List<EmployeeDTO> getTestData() {
        EmployeeDTO employee1 = new EmployeeDTO();
        employee1.setId(0L);
        employee1.setFirstName("Jan");
        employee1.setPassword(bCryptPasswordEncoder.encode("123"));
        employee1.setUserName("pko");
        employee1.setSurName("kowalski");
        employee1.setPhoneNumber(Long.valueOf("999999999"));
        employee1.setIsActive(false);

        EmployeeDTO employee2 = new EmployeeDTO();
        employee1.setId(1L);
        employee2.setFirstName("Janek");
        employee2.setPassword(bCryptPasswordEncoder.encode("LOL"));
        employee2.setUserName("pawel");
        employee2.setSurName("kowalski2");
        employee2.setPhoneNumber(Long.valueOf("999999999"));
        employee2.setIsActive(true);

        return Arrays.asList(employee1, employee2);
    }

    @Test
    public void getEmployee() throws Exception {


    }

    @Test
    public void getEmployees() throws Exception {

    }

    @Test
    public void addEmployee() {
    }

    @Test
    public void updateEmployee() {
    }

    @Test
    public void deleteEmployee() {
    }

    @Test
    public void patchEmployee() {
    }
}