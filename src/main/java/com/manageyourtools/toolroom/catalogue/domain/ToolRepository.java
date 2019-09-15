package com.manageyourtools.toolroom.catalogue.domain;

import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

interface ToolRepository extends Repository<Tool, Long> {

    Tool save(Tool tool);
    Optional<Tool> findById(Long id);
    Set<Tool> findAll();
    Set<Tool> findAllByCategoryId(Long categoryId);
    Set<Tool> findAllByWarrantyDateBefore(LocalDate date);

    @Query(value = "SELECT T FROM Tool t WHERE t.allCount < t.minimalCount")
    Set<Tool> findAllWithCountSmallerThanMinimal();

    default Tool getToolOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResourceNotFoundException("Tool with id=" +  id + " not found"));
    }


}
