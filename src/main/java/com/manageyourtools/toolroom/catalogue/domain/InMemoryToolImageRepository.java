package com.manageyourtools.toolroom.catalogue.domain;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
class InMemoryToolImageRepository implements ToolImageRepository {

    private final Map<Long, ToolImage> db;

    @Override
    public void save(ToolImage toolImage) {
        db.put(toolImage.getToolId(), toolImage);
    }

    @Override
    public Optional<ToolImage> findByToolId(Long toolId) {
        return Optional.of(db.get(toolId));
    }
}
