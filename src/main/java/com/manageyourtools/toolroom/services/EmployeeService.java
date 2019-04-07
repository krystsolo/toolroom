package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.api.model.EmployeeShortDTO;
import com.manageyourtools.toolroom.domains.Employee;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO getEmployeeDtoById(Long id);
    Employee getEmployeeById(Long id);
    Employee getEmployeeByUsername(String username);
    List<EmployeeShortDTO> getEmployees();
    EmployeeDTO deleteEmployee(Long id);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);
    EmployeeShortDTO getShortInfoAboutEmployeeById(Long id);
    Employee getLoggedEmployee();
}
