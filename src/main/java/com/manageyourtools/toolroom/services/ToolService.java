package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.model.ToolDTO;
import com.manageyourtools.toolroom.domains.Tool;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ToolService {

    List<ToolDTO> findAllTools();
    ToolDTO getToolDto(Long id);
    Tool findToolById(Long id);
    ToolDTO saveTool(ToolDTO toolDTO);
    ToolDTO updateTool(Long id, ToolDTO toolDTO);
    void deleteTool(Long id);
}
