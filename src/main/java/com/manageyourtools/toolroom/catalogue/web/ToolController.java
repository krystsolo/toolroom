package com.manageyourtools.toolroom.catalogue.web;

import com.manageyourtools.toolroom.catalogue.domain.CatalogueFacade;
import com.manageyourtools.toolroom.catalogue.domain.Interval;
import com.manageyourtools.toolroom.catalogue.dto.ToolDto;
import com.manageyourtools.toolroom.catalogue.dto.ToolQueryDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(ToolController.BASE_URL)
@AllArgsConstructor
class ToolController {

    static final String BASE_URL = "/tools";
    private final CatalogueFacade catalogueFacade;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ToolQueryDto getTool(@PathVariable Long id){
        return catalogueFacade.findToolById(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Set<ToolQueryDto> getTools(@RequestParam(required = false, defaultValue = "") String warranty,
                                      @RequestParam(required = false, defaultValue = "false") boolean toolsShortages){
        if (!warranty.isEmpty() && toolsShortages) {
            throw new IllegalArgumentException("Not invalid parameters combination for tools find");
        }
        if (!warranty.isEmpty()) {
            return catalogueFacade.findToolsWithCloseWarrantyTime(Interval.valueOf(warranty));
        } else if (toolsShortages) {
            return catalogueFacade.findToolsWithCountSmallerThanMinimal();
        }
        return catalogueFacade.findAllTools();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ToolQueryDto addTool(@RequestBody ToolDto toolDto){
        return catalogueFacade.saveTool(toolDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ToolQueryDto updateTool(@RequestBody ToolDto toolDto, @PathVariable Long id) {
        if (!toolDto.getId().equals(id)) {
            throw new IllegalArgumentException("Tool ID " + toolDto.getId() + " does not match id " + id + " from path");
        }
        return catalogueFacade.saveTool(toolDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTool(@PathVariable Long id){
        catalogueFacade.disableTool(id);
    }
}
