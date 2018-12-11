package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.LendingOrder;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LendingOrderService {

    List<LendingOrder> findAllLendingOrders();
    LendingOrder findLendingOrderById(Long id);
    void deleteLendingOrder(Long id);
    LendingOrder addLendingOrder(LendingOrder lendingOrder);
    LendingOrder updateLendingOrder(Long id, LendingOrder lendingOrder);
    LendingOrder patchLendingOrder(Long id, LendingOrder lendingOrder);
}
