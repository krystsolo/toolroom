package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.EmployeeMapper;
import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.config.AuthenticationFacade;
import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.EmployeeRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.manageyourtools.toolroom.services.UserDetailsServiceImpl.HAS_ROLE_ADMIN;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmployeeMapper employeeMapper;
    private final AuthenticationFacade authenticationFacade;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationFacade authenticationFacade, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.employeeMapper = employeeMapper;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {

        return employeeRepository.findById(id).map(employeeMapper::employeeToEmployeeDTO).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public EmployeeDTO getEmployeeByUsername(String username) {

        Employee employee = employeeRepository.findByUserName(username);

        if(employee == null) {
            throw new ResourceNotFoundException("Employee not found");
        }
        return employeeMapper.employeeToEmployeeDTO(employee);
    }

    @Override
    public List<EmployeeDTO> getEmployees() {
        return employeeRepository.findAll().stream().map(employeeMapper::employeeToEmployeeDTO).collect(Collectors.toList());
    }

    @Override
    @PreAuthorize(HAS_ROLE_ADMIN)
    public EmployeeDTO deleteEmployee(Long id) {

        return employeeRepository.findById(id).map(employee -> {
                    if (authenticationFacade.getUsernameOfCurrentLoggedUser().equals(employee.getUserName())) {
                        throw new IllegalArgumentException("Cannot deactivate current logged user");
                    }
                    employee.setIsActive(false);
                    Employee savedEmployee = employeeRepository.save(employee);

                    return employeeMapper.employeeToEmployeeDTO(savedEmployee);
                }
        )
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @PreAuthorize(HAS_ROLE_ADMIN)
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Optional<Employee> employeeToUpdate = employeeRepository.findById(id);

        if(!employeeToUpdate.isPresent()){
            return saveEmployee(employeeDTO);
        }
        Employee employee = employeeMapper.employeeDtoToEmployee(employeeDTO);
        employee.setId(employeeToUpdate.get().getId());

        return employeeMapper.employeeToEmployeeDTO(employeeRepository.save(employee));
    }

    @Override
    @PreAuthorize(HAS_ROLE_ADMIN)
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.employeeDtoToEmployee(employeeDTO);
        employee.setPassword(bCryptPasswordEncoder.encode(employeeDTO.getPassword()));
        employee.setIsActive(true);

        return employeeMapper.employeeToEmployeeDTO(employeeRepository.save(employee));
    }

    @Override
    @PreAuthorize(HAS_ROLE_ADMIN)
    public EmployeeDTO patchEmployee(Long id, EmployeeDTO employeeDTO) {

        return employeeRepository.findById(id).map(updatedEmployee -> {
            Employee employee = employeeMapper.employeeDtoToEmployee(employeeDTO);

            if (employee.getFirstName() != null) {
                updatedEmployee.setFirstName(employee.getFirstName());
            }

            if (employee.getSurName() != null) {
                updatedEmployee.setSurName(employee.getSurName());
            }

            if (employee.getUserName() != null) {
                updatedEmployee.setUserName(employee.getUserName());
            }

            if (employee.getIsActive() != null) {
                updatedEmployee.setIsActive(employee.getIsActive());
            }

            if (employee.getPassword() != null) {
                updatedEmployee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
            }

            if (employee.getImage() != null) {
                updatedEmployee.setImage(employee.getImage());
            }

            if (employee.getPhoneNumber() != null) {
                updatedEmployee.setPhoneNumber(employee.getPhoneNumber());
            }

            if (employee.getWorkingGroup() != null) {
                updatedEmployee.setWorkingGroup(employee.getWorkingGroup());
            }

            if (employee.getEmail() != null) {
                updatedEmployee.setEmail(employee.getEmail());
            }

            if (employee.getRoles() != null) {
                updatedEmployee.setRoles(employee.getRoles());
            }

            return employeeMapper.employeeToEmployeeDTO(employeeRepository.save(updatedEmployee));

        }).orElseThrow(ResourceNotFoundException::new);
    }
}
