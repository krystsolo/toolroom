package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(exported = false)
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUserName(String userName);
}
