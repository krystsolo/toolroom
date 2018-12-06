package com.manageyourtools.toolroom.controllers.assembler;

import com.manageyourtools.toolroom.api.model.ToolDTO;
import com.manageyourtools.toolroom.controllers.ToolController;
import com.manageyourtools.toolroom.domains.Tool;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ToolResourceAssembler implements ResourceAssembler<ToolDTO, Resource<ToolDTO>> {

    @Override
    public Resource<ToolDTO> toResource(ToolDTO tool) {

        return new Resource<>(tool,
                ControllerLinkBuilder.linkTo(methodOn(ToolController.class).getTool(tool.getId())).withSelfRel(),
                linkTo(methodOn(ToolController.class).getTools(Pageable.unpaged(),null)).withRel("tools"));
    }
}
