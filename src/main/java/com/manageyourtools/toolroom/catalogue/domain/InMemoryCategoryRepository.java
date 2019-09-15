package com.manageyourtools.toolroom.catalogue.domain;

import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
class InMemoryCategoryRepository implements CategoryRepository {

    private final Map<Long, Category> db;

    @Override
    public Category save(Category category) {
        return db.put(category.toDto().getId(), category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Set<Category> findAll() {
        return new HashSet<>(db.values());
    }

    @Override
    public void deleteById(Long id) {
        db.remove(id);
    }
}
