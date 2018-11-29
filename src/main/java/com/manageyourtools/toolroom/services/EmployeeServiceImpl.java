package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.config.AuthenticationFacade;
import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.repositories.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.manageyourtools.toolroom.services.UserDetailsServiceImpl.HAS_ROLE_ADMIN;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationFacade authenticationFacade) {
        this.employeeRepository = employeeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Employee getEmployeeById(Long id) {

        return employeeRepository.findById(id).orElseThrow(RuntimeException::new);//todo better error handling
    }

    @Override
    public Page<Employee> getEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    @PreAuthorize(HAS_ROLE_ADMIN)
    public void deleteEmployee(Long id) {

        employeeRepository.findById(id).ifPresent(employee -> {
            employee.setIsActive(false);
            employeeRepository.save(employee);
        });

    }

    @Override
    @PreAuthorize(HAS_ROLE_ADMIN)
    public Employee updateEmployee(Long id, Employee employee) {

        employee.setId(id);

        return employeeRepository.save(employee);
    }

    @Override
    @PreAuthorize(HAS_ROLE_ADMIN)
    public Employee saveEmployee(Employee employee) {

        employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));

        return employeeRepository.save(employee);
    }

    @Override
    @PreAuthorize(HAS_ROLE_ADMIN)
    public Employee patchEmployee(Long id, Employee employee) {

        return employeeRepository.findById(id).map(updatedEmployee -> {

            if(employee.getFirstName() != null){
                updatedEmployee.setFirstName(employee.getFirstName());
            }

            if(employee.getSurName() != null){
                updatedEmployee.setSurName(employee.getSurName());
            }

            if(employee.getUserName() != null){
                updatedEmployee.setUserName(employee.getUserName());
            }

            if(employee.getIsActive() != null){
                updatedEmployee.setIsActive(employee.getIsActive());
            }

            if(employee.getPassword() != null){
                updatedEmployee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
            }

            if(employee.getImage() != null){
                updatedEmployee.setImage(employee.getImage());
            }

            if(employee.getPhoneNumber() != null){
                updatedEmployee.setPhoneNumber(employee.getPhoneNumber());
            }

            if(employee.getWorkingGroup() != null){
                updatedEmployee.setWorkingGroup(employee.getWorkingGroup());
            }

            if(employee.getEmail() != null){
                updatedEmployee.setEmail(employee.getEmail());
            }

            if(employee.getRoles() != null){
                updatedEmployee.setRoles(employee.getRoles());
            }

            return employeeRepository.save(updatedEmployee);

        }).orElseThrow(RuntimeException::new);//todo better exception handling
    }
}
