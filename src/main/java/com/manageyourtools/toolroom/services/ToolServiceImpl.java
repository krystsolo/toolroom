package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.ToolMapper;
import com.manageyourtools.toolroom.api.model.ToolDTO;
import com.manageyourtools.toolroom.domains.Tool;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.ToolRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.manageyourtools.toolroom.services.UserDetailsServiceImpl.HAS_ROLE_WAREHOUSEMAN;

@Service
public class ToolServiceImpl implements ToolService {

    private final ToolRepository toolRepository;
    private final ToolMapper toolMapper;

    public ToolServiceImpl(ToolRepository toolRepository, ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.toolMapper = toolMapper;
    }

    @Override
    public List<ToolDTO> findAllTools() {
        return toolRepository.findAll().stream().map(toolMapper::toolToToolDTO).collect(Collectors.toList());
    }

    @Override
    public ToolDTO findTool(Long id) {
        return toolRepository.findById(id)
                .map(toolMapper::toolToToolDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public ToolDTO saveTool(ToolDTO toolDTO) {
        Tool tool = toolMapper.toolDtoToTool(toolDTO);

        tool.setAllCount(0L);
        tool.setCurrentCount(0L);
        tool.setIsEnable(true);

        return toolMapper.toolToToolDTO(toolRepository.save(tool));
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public ToolDTO updateTool(Long id, ToolDTO toolDTO) {

        toolDTO.setId(id);
        Optional<Tool> toolOptional = toolRepository.findById(id);

        if (!toolOptional.isPresent()) {
            return saveTool(toolDTO);
        }

        Tool tool = toolMapper.toolDtoToTool(toolDTO);
        Tool savedTool = toolOptional.get();

        if (savedTool.getAllCount() > 1 && tool.getIsUnique()) {
            throw new IllegalArgumentException("Unique tool cannot be in number bigger than 1");
        }

        tool.setAllCount(savedTool.getAllCount());
        tool.setCurrentCount(savedTool.getCurrentCount());
        tool.setIsEnable(savedTool.getIsEnable());

        return toolMapper.toolToToolDTO(toolRepository.save(tool));
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public void deleteTool(Long id) {

        toolRepository.findById(id).ifPresent(tool -> {
            if (tool.getBuyOrderTools().size() != 0) {
                if (tool.getAllCount() == 0) {
                    tool.setIsEnable(false);
                    toolRepository.save(tool);
                } else {
                    throw new IllegalArgumentException("Tool can not be deleted, because there are still " + tool.getAllCount() + " tools in the warehouse.");
                }

            } else {
                toolRepository.deleteById(id);
            }
        });
    }


    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public ToolDTO patchTool(Long id, ToolDTO toolDTO) {
        return toolRepository.findById(id).map(updatedTool -> {

            Tool tool = toolMapper.toolDtoToTool(toolDTO);

            if (tool.getIsToReturn() != null) {
                updatedTool.setIsToReturn(tool.getIsToReturn());
            }

            if (tool.getIsUnique() != null) {
                if (updatedTool.getAllCount() > 1 && tool.getIsUnique()) {
                    throw new IllegalArgumentException("Unique tool cannot be in number bigger than 1");
                }
                updatedTool.setIsUnique(tool.getIsUnique());
            }

            if (tool.getMinimalCount() != null) {
                updatedTool.setMinimalCount(tool.getMinimalCount());
            }

            if (tool.getCategory() != null) {
                updatedTool.setCategory(tool.getCategory());
            }

            if (tool.getLocation() != null) {
                updatedTool.setLocation(tool.getLocation());
            }

            if (tool.getName() != null) {
                updatedTool.setName(tool.getName());
            }

            if (tool.getWarrantyDate() != null) {
                updatedTool.setWarrantyDate(tool.getWarrantyDate());
            }

            return toolMapper.toolToToolDTO(updatedTool);

        }).orElseThrow(ResourceNotFoundException::new);
    }
}
