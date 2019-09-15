package com.manageyourtools.toolroom.catalogue.domain;

import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import org.springframework.data.repository.Repository;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

interface CategoryRepository extends Repository<Category, Long> {

    Category save(Category category);
    Optional<Category> findById(Long id);
    Set<Category> findAll();
    void deleteById(Long id);

    default Category getCategoryOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResourceNotFoundException("Category with id=" +  id + " not found"));
    }
}
