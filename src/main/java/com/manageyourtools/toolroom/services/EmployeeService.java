package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.domains.Employee;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO getEmployeeById(Long id);
    EmployeeDTO getEmployeeByUsername(String username);
    List<EmployeeDTO> getEmployees();
    EmployeeDTO deleteEmployee(Long id);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO patchEmployee(Long id, EmployeeDTO employeeDTO);
}
