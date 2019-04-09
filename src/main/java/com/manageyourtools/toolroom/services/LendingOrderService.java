package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.model.LendingOrderDTO;
import com.manageyourtools.toolroom.domains.LendingOrder;

import java.util.List;

public interface LendingOrderService {

    List<LendingOrderDTO> findAllLendingOrders();

    LendingOrderDTO findLendingOrderDTOById(Long id);

    LendingOrder findLendingOrderById(Long id);
    void deleteLendingOrder(Long id);
    LendingOrderDTO addLendingOrder(LendingOrderDTO lendingOrder);
    LendingOrderDTO updateLendingOrder(Long id, LendingOrderDTO lendingOrder);
}
