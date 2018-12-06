package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.BuyOrderTool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyOrderToolRepository extends JpaRepository<BuyOrderTool, Long> {
}
