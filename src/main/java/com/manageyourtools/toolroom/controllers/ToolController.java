package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.ToolDTO;
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
    public Resource<ToolDTO> getTool(@PathVariable Long id){

        ToolDTO toolDTO = toolService.findTool(id);

        return assembler.toResource(toolDTO);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PagedResources<Resource<ToolDTO>> getTools(Pageable pageable, PagedResourcesAssembler<ToolDTO> pagedResourcesAssembler){

        return pagedResourcesAssembler.toResource(
                toolService.findAllTools(pageable),
                assembler,
                linkTo(methodOn(ToolController.class).getTools(pageable,pagedResourcesAssembler)).withSelfRel());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<ToolDTO> addTool(@RequestBody ToolDTO toolDTO){

        return assembler.toResource(toolService.saveTool(toolDTO));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<ToolDTO> updateTool(@RequestBody ToolDTO toolDTO, @PathVariable Long id) {

        return assembler.toResource(toolService.updateTool(id, toolDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTool(@PathVariable Long id){

        toolService.deleteTool(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<ToolDTO> patchTool(@PathVariable Long id, @RequestBody ToolDTO toolDTO) {

        return assembler.toResource(toolService.patchTool(id, toolDTO));
    }
}
