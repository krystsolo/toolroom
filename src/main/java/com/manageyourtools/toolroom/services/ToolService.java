package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.Tool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ToolService {

    Page<Tool> findAllTools(Pageable pageable);
    Tool findTool(Long id);
    Tool saveTool(Tool tool);
    Tool updateTool(Long id, Tool tool);
    void deleteTool(Long id);
    Tool patchTool(Long id, Tool tool);
}
