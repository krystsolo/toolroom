package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.LendingOrderTool;
import com.manageyourtools.toolroom.domains.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LendingOrderToolRepository extends JpaRepository<LendingOrderTool, Long> {
    List<LendingOrderTool> findAllByTool(Tool tool);
}
