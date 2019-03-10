package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.CategoryMapper;
import com.manageyourtools.toolroom.api.model.CategoryDTO;
import com.manageyourtools.toolroom.domains.Category;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.CategoryRepository;
import com.manageyourtools.toolroom.repositories.ToolRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.manageyourtools.toolroom.services.UserDetailsServiceImpl.HAS_ROLE_WAREHOUSEMAN;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ToolRepository toolRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, ToolRepository toolRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.toolRepository = toolRepository;
    }

    @Override
    public List<CategoryDTO> findAllCategories(Sort sort) {

        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::categoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO findCategoryById(Long id) {

        return categoryRepository.findById(id)
                .map(categoryMapper::categoryToCategoryDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public void deleteCategory(Long id) {

        categoryRepository
                .findById(id)
                .ifPresent(category -> {
                            toolRepository.findAllByCategory(category)
                                    .forEach(
                                            tool -> tool.setCategory(null)
                                    );
                            categoryRepository.deleteById(id);
                        }
                );
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {

        Category category = categoryRepository.save(categoryMapper.categoryDTOToCategory(categoryDTO));

        return categoryMapper.categoryToCategoryDTO(category);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {

        categoryDTO.setId(id);
        Category category = categoryRepository.save(categoryMapper.categoryDTOToCategory(categoryDTO));

        return categoryMapper.categoryToCategoryDTO(category);
    }
}
