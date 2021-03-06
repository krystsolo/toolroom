package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.api.model.EmployeeShortDTO;
import com.manageyourtools.toolroom.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(EmployeeController.BASE_URL)
public class EmployeeController {

    public static final String BASE_URL = "/employees";

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO getEmployee(@PathVariable String id){

        return employeeService.getEmployeeDtoById(Long.valueOf(id));
    }

    @GetMapping("/{id}/info")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeShortDTO getShortInfoAboutEmployee(@PathVariable String id){

        return employeeService.getShortInfoAboutEmployeeById(Long.valueOf(id));
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeShortDTO> getEmployees(){

        return employeeService.getEmployees();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO addEmployee(@RequestBody EmployeeDTO employeeDTO){

        return employeeService.saveEmployee(employeeDTO);


    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable String id){

        return employeeService.updateEmployee(Long.valueOf(id), employeeDTO);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO deleteEmployee(@PathVariable Long id){

        return employeeService.deleteEmployee(id);
    }
}
