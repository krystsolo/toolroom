package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.BuyOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BuyOrderService {

    Page<BuyOrder> findAllBoughts(Pageable pageable);
    BuyOrder findBoughtById(Long id);
    void deleteBought(Long id);
    BuyOrder addBought(BuyOrder buyOrder);
    BuyOrder updateBought(Long id, BuyOrder buyOrder);
    BuyOrder patchBought(Long id, BuyOrder buyOrder);
}
