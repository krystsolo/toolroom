package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.model.ToolDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ToolService {

    List<ToolDTO> findAllTools();
    ToolDTO findTool(Long id);
    ToolDTO saveTool(ToolDTO toolDTO);
    ToolDTO updateTool(Long id, ToolDTO toolDTO);
    void deleteTool(Long id);
    ToolDTO patchTool(Long id, ToolDTO toolDTO);
}
