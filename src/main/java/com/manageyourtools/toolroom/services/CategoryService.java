package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.Category;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CategoryService {

    List<Category> findAllCategories(Sort sort);
    Category findCategoryById(Long id);
    void deleteCategory(Long id);
    Category addCategory(Category category);
    Category updateCategory(Long id, Category category);
}
