package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.controllers.assembler.EmployeeResourceAssembler;
import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.services.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.config.HateoasAwareSpringDataWebConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(HateoasAwareSpringDataWebConfiguration.class)
public class EmployeeControllerTest {

    @Mock
    EmployeeService employeeService;

    @InjectMocks
    EmployeeController controller;

    MockMvc mockMvc;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    EmployeeResourceAssembler employeeResourceAssembler;

    @Mock
    PagedResourcesAssembler pagedResourcesAssembler;

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
        EmployeeDTO employee = getTestData().get(0);

        Resource<EmployeeDTO> employeeResource = new Resource<>(employee);
        when(employeeService.getEmployeeById(anyLong())).thenReturn(employee);
        when(employeeResourceAssembler.toResource(any(EmployeeDTO.class))).thenReturn(employeeResource);

        mockMvc.perform(get(EmployeeController.BASE_URL + "/0")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", equalTo("pko")));

    }

    @Test
    public void getEmployees() throws Exception {
        Page<EmployeeDTO> employees = new PageImpl<>(getTestData(), Pageable.unpaged(),getTestData().size());

        List<Resource<EmployeeDTO>> employeeResource = employees.stream().map(employee -> new Resource<>(employee)).collect(Collectors.toList());

        when(employeeService.getEmployees(Pageable.unpaged())).thenReturn(employees);

        //when(pagedResourcesAssembler.toResource(any(), any(EmployeeResourceAssembler.class))).thenReturn();

        mockMvc.perform(get(EmployeeController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeList", hasSize(2)));
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