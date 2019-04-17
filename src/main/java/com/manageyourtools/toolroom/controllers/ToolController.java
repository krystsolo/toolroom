package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.ToolDTO;

import com.manageyourtools.toolroom.services.ToolService;

import org.apache.tomcat.jni.Local;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(ToolController.BASE_URL)
public class ToolController {

    protected static final String BASE_URL = "/tools";
    private ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ToolDTO getTool(@PathVariable Long id){

        return toolService.getToolDto(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<ToolDTO> getTools(@RequestParam(required = false, defaultValue = "") String warranty,
                                  @RequestParam(required = false, defaultValue = "false") boolean toolsShortages){
        if (!warranty.isEmpty() && toolsShortages) {
            throw new IllegalArgumentException("Not invalid parameters combination for tools find");
        }
        if (!warranty.isEmpty()) {
            return toolService.findToolsWithCloseWarrantyTime(warranty);
        } else if (toolsShortages) {
            return toolService.getToolsWithCountSmallerThanMinimal();
        }
        return toolService.findAllTools();
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
}
