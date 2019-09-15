package com.manageyourtools.toolroom.catalogue.domain;


import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
interface ToolImageRepository extends Repository<ToolImage, Long> {
    void save(ToolImage toolImage);
    Optional<ToolImage> findByToolId(Long toolId);

    default ToolImage getImageByToolIdOrThrow(Long id) {
        return findByToolId(id).orElseThrow(() -> new ResourceNotFoundException("Image for tool with id=" +  id + " not found"));
    }
}
