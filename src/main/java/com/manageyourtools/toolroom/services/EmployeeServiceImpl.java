package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.EmployeeMapper;
import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.api.model.EmployeeShortDTO;
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
    @PreAuthorize(HAS_ROLE_ADMIN)
    public EmployeeDTO getEmployeeDtoById(Long id) {
        return employeeMapper.employeeToEmployeeDTO(getEmployeeById(id));
    }

    @Override
    public Employee getEmployeeByUsername(String username) {

        return employeeRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with username=" +  username + " not found"));
    }

    @Override
    public EmployeeShortDTO getShortInfoAboutEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::employeeToEmployeeShortDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id=" +  id + " not found"));
    }

    @Override
    public List<EmployeeShortDTO> getEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::employeeToEmployeeShortDTO)
                .collect(Collectors.toList());
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
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id=" +  id + " not found"));
    }

    @Override
    @PreAuthorize(HAS_ROLE_ADMIN)
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Optional<Employee> employeeToUpdate = employeeRepository.findById(id);

        if (!employeeToUpdate.isPresent()){
            return saveEmployee(employeeDTO);
        }
        if (employeeDTO.getPassword() == null || employeeDTO.getPassword().equals("")) {
            employeeDTO.setPassword(employeeToUpdate.get().getPassword());
        } else {
            employeeDTO.setPassword(bCryptPasswordEncoder.encode(employeeDTO.getPassword()));
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

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id=" +  id + " not found"));
    }

    public Employee getLoggedEmployee() {
        return this.getEmployeeByUsername(
                authenticationFacade.getUsernameOfCurrentLoggedUser());
    }
}
