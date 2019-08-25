package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.CategoryMapper;
import com.manageyourtools.toolroom.api.model.CategoryDTO;
import com.manageyourtools.toolroom.domains.Category;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.CategoryRepository;
import com.manageyourtools.toolroom.repositories.ToolRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.manageyourtools.toolroom.services.UserDetailsServiceImpl.HAS_ROLE_WAREHOUSEMAN;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ToolRepository toolRepository;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, ToolRepository toolRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.toolRepository = toolRepository;
    }

    public List<CategoryDTO> findAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::categoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::categoryToCategoryDTO)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with id=" + id + " not found"));
    }

    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public void deleteCategory(Long id) {
        categoryRepository
                .findById(id)
                .ifPresent(category -> {
                    toolRepository.findAllByCategory(category)
                            .forEach(tool ->
                                    tool.setCategory(null));
                    categoryRepository.deleteById(id);
                });
    }

    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category = categoryRepository.save(categoryMapper.categoryDTOToCategory(categoryDTO));
        return categoryMapper.categoryToCategoryDTO(category);
    }

    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        categoryDTO.setId(id);
        Category category = categoryRepository.save(
                categoryMapper.categoryDTOToCategory(categoryDTO));
        return categoryMapper.categoryToCategoryDTO(category);
    }
}
