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
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeResourceAssembler assembler;

    public EmployeeController(EmployeeService employeeService, EmployeeResourceAssembler assembler, AuthenticationFacade authenticationFacade) {
        this.employeeService = employeeService;
        this.assembler = assembler;
    }

    @GetMapping("/employees/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<Employee> getEmployee(@PathVariable Long id){

        Employee employee = employeeService.getEmployeeById(id);

        return assembler.toResource(employee);
    }

    @GetMapping("/employees")
    @ResponseStatus(HttpStatus.OK)
    public PagedResources<Resource<Employee>> getEmployees(Pageable pageable, PagedResourcesAssembler<Employee> pagedResourcesAssembler){

        //Page<Resource<Employee>> resourcesPage = employeeService.getEmployees(pageable).map(assembler::toResource);
        return pagedResourcesAssembler.toResource(
                employeeService.getEmployees(pageable),
                assembler,
                linkTo(methodOn(EmployeeController.class).getEmployees(pageable,pagedResourcesAssembler)).withSelfRel());
//        return new PagedResources<>(resourcesPage.getContent(),
//                new PagedResources.PageMetadata(
//                        resourcesPage.getSize(), resourcesPage.getNumber(),
//                        resourcesPage.getTotalElements(), resourcesPage.getTotalPages()),
//                linkTo(methodOn(EmployeeController.class).getEmployees(pageable)).withSelfRel());
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<Employee> addEmployee(@RequestBody Employee employee){

        return assembler.toResource(employeeService.saveEmployee(employee));


    }

    @PutMapping("/employees/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<Employee> updateEmployee(@RequestBody Employee employee, @PathVariable Long id){

        return assembler.toResource(employeeService.updateEmployee(id, employee));

    }

    @DeleteMapping("/employees/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@PathVariable Long id){

        employeeService.deleteEmployee(id);
    }

    @PatchMapping("/employees/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<Employee> patchEmployee(@PathVariable Long id, @RequestBody Employee employee){

        return assembler.toResource(employeeService.patchEmployee(id, employee));
    }
}
