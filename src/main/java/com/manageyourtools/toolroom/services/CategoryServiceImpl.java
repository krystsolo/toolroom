package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.Category;
import com.manageyourtools.toolroom.repositories.CategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAllCategories(Sort sort) {

        return categoryRepository.findAll(sort);
    }

    @Override
    public Category findCategoryById(Long id) {

        return categoryRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public void deleteCategory(Long id) {

        categoryRepository.deleteById(id);
    }

    @Override
    public Category addCategory(Category category) {

        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category category) {

        category.setId(id);

        return categoryRepository.save(category);
    }
}
