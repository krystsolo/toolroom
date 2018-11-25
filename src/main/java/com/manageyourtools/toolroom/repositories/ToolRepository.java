package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
}
