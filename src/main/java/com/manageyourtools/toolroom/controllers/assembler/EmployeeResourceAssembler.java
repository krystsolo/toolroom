package com.manageyourtools.toolroom.controllers.assembler;

import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.controllers.EmployeeController;
import com.manageyourtools.toolroom.domains.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class EmployeeResourceAssembler implements ResourceAssembler<EmployeeDTO, Resource<EmployeeDTO>> {


    @Override
    public Resource<EmployeeDTO> toResource(EmployeeDTO employee) {

        return new Resource<>(employee,
                ControllerLinkBuilder.linkTo(methodOn(EmployeeController.class).getEmployee(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getEmployees(Pageable.unpaged(),null)).withRel("employees"));
    }
}
