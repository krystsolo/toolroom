package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.LendingReturnOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LendingReturnOrderRepository extends JpaRepository<LendingReturnOrder, Long> {
}
