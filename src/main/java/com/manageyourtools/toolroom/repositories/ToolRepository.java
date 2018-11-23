package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(exported = false)
public interface ToolRepository extends JpaRepository<Tool, Long> {
}
