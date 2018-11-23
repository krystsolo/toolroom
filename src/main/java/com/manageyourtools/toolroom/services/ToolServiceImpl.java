package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.Tool;
import com.manageyourtools.toolroom.repositories.ToolRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolServiceImpl implements ToolService {

    private ToolRepository toolRepository;

    public ToolServiceImpl(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    @Override
    public Page<Tool> findAllTools(Pageable pageable) {
        return toolRepository.findAll(pageable);
    }

    @Override
    public Tool findTool(Long id) {
        return toolRepository.findById(id).orElseThrow(RuntimeException::new);//todo add bettter handling
    }

    @Override
    public Tool saveTool(Tool tool) {
        return toolRepository.save(tool);
    }

    @Override
    public Tool updateTool(Long id, Tool tool) {
        tool.setId(id);
        return toolRepository.save(tool);
    }

    @Override
    public void deleteTool(Long id) {
        toolRepository.deleteById(id);
    }

    @Override
    public Tool patchTool(Long id, Tool tool) {
        return null;//todo patchToolService
    }
}
