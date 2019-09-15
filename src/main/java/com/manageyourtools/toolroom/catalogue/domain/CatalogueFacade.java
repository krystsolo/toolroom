package com.manageyourtools.toolroom.catalogue.domain;

import com.manageyourtools.toolroom.catalogue.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional
public class CatalogueFacade {

    private final ToolRepository toolRepository;
    private final ToolFactory toolFactory;
    private final ToolImageRepository toolImageRepository;
    private final CategoryFactory categoryFactory;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Set<ToolQueryDto> findAllTools() {
        return toolRepository.findAll().stream().map(Tool::toQuery).collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public ToolQueryDto findToolById(Long id) {
        return toolRepository.getToolOrThrow(id).toQuery();
    }

    public ToolQueryDto saveTool(ToolDto toolDto) {
        Tool tool = toolFactory.from(toolDto);
        tool = toolRepository.save(tool);
        return tool.toQuery();
    }

    public void disableTool(Long id) {
        Tool tool = toolRepository.getToolOrThrow(id);
        tool.disable();
    }

    public void calculateToolAllQuantity(ToolAllQuantityChange toolAllQuantityChange) {
        Tool tool = toolRepository.getToolOrThrow(toolAllQuantityChange.getToolId());
        tool.changeAllQuantity(toolAllQuantityChange.getQuantityChange());
    }

    public void calculateToolCurrentQuantity(ToolCurrentQuantityChange toolCurrentQuantityChange) {
        Tool tool = toolRepository.getToolOrThrow(toolCurrentQuantityChange.getToolId());
        tool.changeCurrentQuantity(toolCurrentQuantityChange.getQuantityChange());
    }

    @Transactional(readOnly = true)
    public Set<ToolQueryDto> findToolsWithCloseWarrantyTime(Interval interval) {
        LocalDate time = calculateDate(interval);
        return toolRepository.findAllByWarrantyDateBefore(time)
                .stream()
                .map(Tool::toQuery)
                .collect(Collectors.toSet());
    }

    private LocalDate calculateDate(Interval interval) {
        LocalDate from;
        if (interval == Interval.WEEK) {
            from = LocalDate.now().minusDays(7);
        } else if (interval == Interval.MONTH) {
            from = LocalDate.now().minusMonths(1);
        } else if (interval == Interval.THREE_MONTHS){
            from = LocalDate.now().minusMonths(3);
        } else {
            throw new IllegalArgumentException("Not valid warranty date option");
        }
        return from;
    }

    @Transactional(readOnly = true)
    public Set<ToolQueryDto> findToolsWithCountSmallerThanMinimal() {
        return toolRepository.findAllWithCountSmallerThanMinimal()
                .stream()
                .map(Tool::toQuery)
                .collect(Collectors.toSet());
    }

    public void uploadImage(MultipartFile file, Long toolId) {
        ToolImage toolImage = toolImageRepository.findByToolId(toolId)
                .orElse(new ToolImage(toolId));
        toolImage.changeImage(file);
    }

    @Transactional(readOnly = true)
    public byte[] findImage(Long toolId) {
        ToolImage toolImage = toolImageRepository.getImageByToolIdOrThrow(toolId);
        return toolImage.retrieveImage();
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(Category::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto findCategory(Long id) {
        return categoryRepository.getCategoryOrThrow(id).toDto();
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
        toolRepository.findAllByCategoryId(id).forEach(tool -> tool.changeCategory(null));
    }

    public CategoryDto saveCategory(CategoryDto categoryDTO) {
        Category category = categoryFactory.from(categoryDTO);
        category = categoryRepository.save(category);
        return category.toDto();
    }
}
