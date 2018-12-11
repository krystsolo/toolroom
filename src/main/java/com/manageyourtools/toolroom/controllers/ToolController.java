package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.ToolDTO;

import com.manageyourtools.toolroom.services.ToolService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tools")
public class ToolController {

    private ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ToolDTO getTool(@PathVariable Long id){

        return toolService.findTool(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Page<ToolDTO> getTools(Pageable pageable){

        return toolService.findAllTools(pageable);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ToolDTO addTool(@RequestBody ToolDTO toolDTO){

        return toolService.saveTool(toolDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ToolDTO updateTool(@RequestBody ToolDTO toolDTO, @PathVariable Long id) {

        return toolService.updateTool(id, toolDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTool(@PathVariable Long id){

        toolService.deleteTool(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ToolDTO patchTool(@PathVariable Long id, @RequestBody ToolDTO toolDTO) {

        return toolService.patchTool(id, toolDTO);
    }
}
