package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    Employee getEmployeeById(Long id);
    Page<Employee> getEmployees(Pageable pageable);
    void deleteEmployee(Long id);
    Employee updateEmployee(Long id, Employee employee);
    Employee saveEmployee(Employee employee);
    Employee patchEmployee(Long id, Employee employee);
}
