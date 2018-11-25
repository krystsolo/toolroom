package com.manageyourtools.toolroom.repositories;

import com.manageyourtools.toolroom.domains.BuyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyOrderRepository extends JpaRepository<BuyOrder, Long> {

}
