package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.model.ToolDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ToolService {

    Page<ToolDTO> findAllTools(Pageable pageable);
    ToolDTO findTool(Long id);
    ToolDTO saveTool(ToolDTO toolDTO);
    ToolDTO updateTool(Long id, ToolDTO toolDTO);
    void deleteTool(Long id);
    ToolDTO patchTool(Long id, ToolDTO toolDTO);
}
