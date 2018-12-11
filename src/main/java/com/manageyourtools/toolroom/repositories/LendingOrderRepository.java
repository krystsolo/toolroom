package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.LendingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LendingOrderRepository extends JpaRepository<LendingOrder, Long> {
}
