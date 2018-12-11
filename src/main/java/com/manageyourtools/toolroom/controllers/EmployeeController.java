package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.services.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


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
    public EmployeeDTO getEmployee(@PathVariable Long id){

        return employeeService.getEmployeeById(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeDTO> getEmployees(@PageableDefault Pageable pageable){

        return employeeService.getEmployees(pageable);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO addEmployee(@RequestBody EmployeeDTO employeeDTO){

        return employeeService.saveEmployee(employeeDTO);


    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable Long id){

        return employeeService.updateEmployee(id, employeeDTO);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO deleteEmployee(@PathVariable Long id){

        return employeeService.deleteEmployee(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeDTO patchEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO){

        return employeeService.patchEmployee(id, employeeDTO);
    }
}
