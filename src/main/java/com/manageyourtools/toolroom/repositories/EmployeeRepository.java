package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUserName(String userName);
}
