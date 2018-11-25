package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.config.AuthenticationFacade;
import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.repositories.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public void deleteEmployee(Long id) {

        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if(employeeOptional.isPresent()){
            Employee employee = employeeOptional.get();
            employee.setIsActive(false);
            employeeRepository.save(employee);
        }
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {

        employee.setId(id);

        return employeeRepository.save(employee);
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));

        return employeeRepository.save(employee);
    }

    @Override
    public Employee patchEmployee(Long id, Employee employee) {

        return employeeRepository.findById(id).map(updatedEmployee -> {

            if(employee.getFirstName() != null){
                updatedEmployee.setFirstName(updatedEmployee.getFirstName());
            }

            if(employee.getSurName() != null){
                updatedEmployee.setSurName(updatedEmployee.getSurName());
            }

            if(employee.getUserName() != null){
                updatedEmployee.setUserName(updatedEmployee.getUserName());
            }

            if(employee.getIsActive() != null){
                updatedEmployee.setIsActive(updatedEmployee.getIsActive());
            }

            if(employee.getPassword() != null){
                updatedEmployee.setPassword(bCryptPasswordEncoder.encode(updatedEmployee.getPassword()));
            }

            if(employee.getImage() != null){
                updatedEmployee.setImage(updatedEmployee.getImage());
            }

            if(employee.getPhoneNumber() != null){
                updatedEmployee.setPhoneNumber(updatedEmployee.getPhoneNumber());
            }

            if(employee.getWorkingGroup() != null){
                updatedEmployee.setWorkingGroup(updatedEmployee.getWorkingGroup());
            }

            if(employee.getEmail() != null){
                updatedEmployee.setEmail(updatedEmployee.getEmail());
            }

            if(employee.getRoles() != null){
                updatedEmployee.setRoles(updatedEmployee.getRoles());
            }

            return employeeRepository.save(updatedEmployee);

        }).orElseThrow(RuntimeException::new);//todo better exception handling
    }
}
