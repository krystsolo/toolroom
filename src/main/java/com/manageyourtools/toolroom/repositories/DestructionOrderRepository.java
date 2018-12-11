package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.DestructionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestructionOrderRepository extends JpaRepository<DestructionOrder, Long> {
}
