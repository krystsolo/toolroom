package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.Category;
import com.manageyourtools.toolroom.domains.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    List<Tool> findAllByCategory(Category category);
}
