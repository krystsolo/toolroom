package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.EmployeeMapperImpl;
import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.config.AuthenticationFacadeImpl;
import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

public class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    EmployeeService employeeService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        employeeService = new EmployeeServiceImpl(employeeRepository, new BCryptPasswordEncoder(), new AuthenticationFacadeImpl(), new EmployeeMapperImpl());
    }

    private List<Employee> getTestData() {
        Employee employee1 = new Employee();
        employee1.setId(0L);
        employee1.setFirstName("Jan");
        employee1.setPassword("123");
        employee1.setUserName("pko");
        employee1.setSurName("kowalski");
        employee1.setPhoneNumber(Long.valueOf("999999999"));
        employee1.setIsActive(false);

        Employee employee2 = new Employee();
        employee1.setId(1L);
        employee2.setFirstName("Janek");
        employee2.setPassword("LOL");
        employee2.setUserName("pawel");
        employee2.setSurName("kowalski2");
        employee2.setPhoneNumber(Long.valueOf("999999999"));
        employee2.setIsActive(true);

        return Arrays.asList(employee1, employee2);
    }

    @Test
    public void testGetEmployeeById() {

        Employee employee = getTestData().get(0);

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(employee));

        EmployeeDTO employeeRetrieved = employeeService.getEmployeeById(2L);

        assertEquals("pko", employeeRetrieved.getUserName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetEmployeeByIdAndNotFoundAny(){
        Optional<Employee> employeeOptional = Optional.empty();

        when(employeeRepository.findById(anyLong())).thenReturn(employeeOptional);

        EmployeeDTO employee = employeeService.getEmployeeById(2L);
    }

    @Test
    public void testGetEmployees() {

        Page<Employee> employees = new PageImpl<>(getTestData(),PageRequest.of(0,2), getTestData().size());

        when(employeeRepository.findAll(Pageable.unpaged())).thenReturn(employees);

        Page<EmployeeDTO> employeePage = employeeService.getEmployees(Pageable.unpaged());

        assertEquals(2, employeePage.getSize());
    }

    @Test
    public void deleteEmployee() {
        Employee employeeBefore = getTestData().get(0);
        employeeBefore.setIsActive(true);

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employeeBefore));

        EmployeeDTO employeeDeleted = employeeService.deleteEmployee(2L);

        assertEquals(false, employeeDeleted.getIsActive());
    }

    @Test
    public void updateEmployee() {
        Employee employee = getTestData().get(0);
        employee.setSurName("NAME");

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setSurName("NAME");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO employeeSaved = employeeService.updateEmployee(2L, employeeDTO);

        then(employeeRepository).should().save(any(Employee.class));
        assertEquals("NAME", employeeSaved.getSurName());
    }

    @Test
    public void saveEmployee() {
        Employee employee = getTestData().get(0);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUserName(employee.getUserName());


        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO employeeSaved = employeeService.saveEmployee(employeeDTO);

        then(employeeRepository).should().save(any(Employee.class));
        assertEquals(employee.getUserName(), employeeSaved.getUserName());
    }

    @Test
    public void patchEmployee() {//todo test patchEmployee
    }
}