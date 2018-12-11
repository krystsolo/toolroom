package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.DestructionOrderTool;
import com.manageyourtools.toolroom.domains.Tool;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestructionOrderToolRepository extends JpaRepository<DestructionOrderTool, Long> {
    List<DestructionOrderTool> findAllByTool(Tool tool);
}
