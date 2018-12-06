package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.config.AuthenticationFacade;
import com.manageyourtools.toolroom.controllers.assembler.EmployeeResourceAssembler;
import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.services.EmployeeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(EmployeeController.BASE_URL)
public class EmployeeController {

    public static final String BASE_URL = "/employees";

    private final EmployeeService employeeService;
    private final EmployeeResourceAssembler assembler;

    public EmployeeController(EmployeeService employeeService, EmployeeResourceAssembler assembler, AuthenticationFacade authenticationFacade) {
        this.employeeService = employeeService;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<EmployeeDTO> getEmployee(@PathVariable Long id){

        EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);

        return assembler.toResource(employeeDTO);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PagedResources<Resource<EmployeeDTO>> getEmployees(@PageableDefault Pageable pageable, PagedResourcesAssembler<EmployeeDTO> pagedResourcesAssembler){

        return pagedResourcesAssembler.toResource(
                employeeService.getEmployees(pageable),
                assembler,
                linkTo(methodOn(EmployeeController.class).getEmployees(pageable,pagedResourcesAssembler)).withSelfRel());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<EmployeeDTO> addEmployee(@RequestBody EmployeeDTO employeeDTO){

        return assembler.toResource(employeeService.saveEmployee(employeeDTO));


    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable Long id){

        return assembler.toResource(employeeService.updateEmployee(id, employeeDTO));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<EmployeeDTO> deleteEmployee(@PathVariable Long id){

        return assembler.toResource(employeeService.deleteEmployee(id));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<EmployeeDTO> patchEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO){

        return assembler.toResource(employeeService.patchEmployee(id, employeeDTO));
    }
}
