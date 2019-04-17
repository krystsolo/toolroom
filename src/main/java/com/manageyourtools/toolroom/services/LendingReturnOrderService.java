package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.model.LendingReturnOrderDTO;

import java.util.List;

public interface LendingReturnOrderService {
    LendingReturnOrderDTO returnLendingOrder(Long id, LendingReturnOrderDTO lendingReturnOrderDTO);

    LendingReturnOrderDTO getLendingReturnOrderDTO(Long id);

    List<LendingReturnOrderDTO> getAllOrders();

    List<LendingReturnOrderDTO> getAllOrdersNotReturnedOnTime();
}
