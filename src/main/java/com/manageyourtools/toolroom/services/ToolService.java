package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.ToolMapper;
import com.manageyourtools.toolroom.api.model.ToolDTO;
import com.manageyourtools.toolroom.domains.Tool;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.ToolRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.manageyourtools.toolroom.services.UserDetailsServiceImpl.HAS_ROLE_WAREHOUSEMAN;

@Service
public class ToolService {

    private final ToolRepository toolRepository;
    private final ToolMapper toolMapper;

    public ToolService(ToolRepository toolRepository, ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.toolMapper = toolMapper;
    }

    public List<ToolDTO> findAllTools() {
        return toolRepository.findAll().stream().map(toolMapper::toolToToolDTO).collect(Collectors.toList());
    }

    public ToolDTO getToolDto(Long id) {
        return toolMapper.toolToToolDTO(findToolById(id));
    }

    public Tool findToolById(Long id) {
        return toolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tool with id=" +  id + " not found"));
    }

    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public ToolDTO saveTool(ToolDTO toolDTO) {
        Tool tool = toolMapper.toolDtoToTool(toolDTO);

        tool.setAllCount(0L);
        tool.setCurrentCount(0L);
        tool.setIsEnable(true);
        return toolMapper.toolToToolDTO(toolRepository.save(tool));
    }

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

    public List<ToolDTO> findToolsWithCloseWarrantyTime(String warranty) {
        LocalDate warrantyDate;
        warrantyDate = calculateDate(warranty);
        return toolRepository.findAllByWarrantyDateBefore(warrantyDate)
                .stream()
                .map(toolMapper::toolToToolDTO)
                .collect(Collectors.toList());
    }

    private LocalDate calculateDate(String warranty) {
        LocalDate warrantyDate;
        if (warranty.equals("week")) {
            warrantyDate = LocalDate.now().minusDays(7);
        } else if (warranty.equals("month")) {
            warrantyDate = LocalDate.now().minusMonths(1);
        } else if (warranty.equals("threemonths")){
            warrantyDate = LocalDate.now().minusMonths(3);
        } else {
            throw new IllegalArgumentException("Not valid warranty date option");
        }
        return warrantyDate;
    }

    public List<ToolDTO> getToolsWithCountSmallerThanMinimal() {
        return toolRepository.findAll()
                .stream()
                .filter(tool -> tool.getMinimalCount() != null && tool.getMinimalCount() > tool.getAllCount())
                .map(toolMapper::toolToToolDTO)
                .collect(Collectors.toList());
    }
}
