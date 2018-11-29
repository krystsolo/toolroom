package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.config.AuthenticationFacade;
import com.manageyourtools.toolroom.controllers.assembler.EmployeeResourceAssembler;
import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.services.EmployeeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeResourceAssembler assembler;

    public EmployeeController(EmployeeService employeeService, EmployeeResourceAssembler assembler, AuthenticationFacade authenticationFacade) {
        this.employeeService = employeeService;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<Employee> getEmployee(@PathVariable Long id){

        Employee employee = employeeService.getEmployeeById(id);

        return assembler.toResource(employee);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PagedResources<Resource<Employee>> getEmployees(Pageable pageable, PagedResourcesAssembler<Employee> pagedResourcesAssembler){

        return pagedResourcesAssembler.toResource(
                employeeService.getEmployees(pageable),
                assembler,
                linkTo(methodOn(EmployeeController.class).getEmployees(pageable,pagedResourcesAssembler)).withSelfRel());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<Employee> addEmployee(@RequestBody Employee employee){

        return assembler.toResource(employeeService.saveEmployee(employee));


    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<Employee> updateEmployee(@RequestBody Employee employee, @PathVariable Long id){

        return assembler.toResource(employeeService.updateEmployee(id, employee));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@PathVariable Long id){

        employeeService.deleteEmployee(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<Employee> patchEmployee(@PathVariable Long id, @RequestBody Employee employee){

        return assembler.toResource(employeeService.patchEmployee(id, employee));
    }
}
