package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.LendingOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LendingOrderService {

    Page<LendingOrder> findAllLendingOrders(Pageable pageable);
    LendingOrder findLendingOrderById(Long id);
    void deleteLendingOrder(Long id);
    LendingOrder addLendingOrder(LendingOrder lendingOrder);
    LendingOrder updateLendingOrder(Long id, LendingOrder lendingOrder);
    LendingOrder patchLendingOrder(Long id, LendingOrder lendingOrder);
}
