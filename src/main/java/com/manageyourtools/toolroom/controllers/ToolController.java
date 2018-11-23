package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.controllers.assembler.ToolResourceAssembler;
import com.manageyourtools.toolroom.domains.Tool;
import com.manageyourtools.toolroom.services.ToolService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/tools")
public class ToolController {

    private ToolService toolService;
    private final ToolResourceAssembler assembler;


    public ToolController(ToolService toolService, ToolResourceAssembler assembler) {
        this.toolService = toolService;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<Tool> getTool(@PathVariable Long id){

       Tool tool = toolService.findTool(id);

        return assembler.toResource(tool);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PagedResources<Resource<Tool>> getTools(Pageable pageable, PagedResourcesAssembler<Tool> pagedResourcesAssembler){


        return pagedResourcesAssembler.toResource(
                toolService.findAllTools(pageable),
                assembler,
                linkTo(methodOn(ToolController.class).getTools(pageable,pagedResourcesAssembler)).withSelfRel());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<Tool> addTool(@RequestBody Tool tool){

        return assembler.toResource(toolService.saveTool(tool));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<Tool> updateTool(@RequestBody Tool tool, @PathVariable Long id) {

        return assembler.toResource(toolService.updateTool(id, tool));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTool(@PathVariable Long id){

        toolService.deleteTool(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<Tool> patchTool(@PathVariable Long id, @RequestBody Tool tool) {

        return assembler.toResource(toolService.patchTool(id, tool));
    }
}
