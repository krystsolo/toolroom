package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.model.CategoryDTO;
import com.manageyourtools.toolroom.domains.Category;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> findAllCategories();
    CategoryDTO findCategoryById(Long id);
    void deleteCategory(Long id);
    CategoryDTO addCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
}
