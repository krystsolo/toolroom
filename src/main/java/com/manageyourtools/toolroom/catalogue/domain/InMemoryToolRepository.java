package com.manageyourtools.toolroom.catalogue.domain;

import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
class InMemoryToolRepository implements ToolRepository {
    private final Map<Long, Tool> db;

    @Override
    public Tool save(Tool tool) {
        return db.put(tool.toDto().getId(), tool);
    }

    @Override
    public Optional<Tool> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Set<Tool> findAll() {
        return new HashSet<>(db.values());
    }

    @Override
    public Set<Tool> findAllByCategoryId(Long categoryId) {
        return db.values().stream().filter(tool -> tool.toDto().getCategory().getId().equals(categoryId)).collect(Collectors.toSet());
    }

    @Override
    public Set<Tool> findAllByWarrantyDateBefore(LocalDate date) {
        return db.values().stream().filter(tool -> tool.toDto().getWarrantyDate().isAfter(date)).collect(Collectors.toSet());
    }

    @Override
    public Set<Tool> findAllWithCountSmallerThanMinimal() {
        return db.values().stream().filter(tool -> tool.toQuery().getMinimalCount() > tool.toQuery().getAllCount()).collect(Collectors.toSet());
    }
}
