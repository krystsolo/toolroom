package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.domains.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeDTO getEmployeeById(Long id);
    Employee getEmployeeByUsername(String username);
    Page<EmployeeDTO> getEmployees(Pageable pageable);
    EmployeeDTO deleteEmployee(Long id);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO patchEmployee(Long id, EmployeeDTO employeeDTO);
}
