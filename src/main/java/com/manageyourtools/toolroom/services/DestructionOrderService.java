package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.model.DestructionOrderDTO;

import java.util.List;

public interface DestructionOrderService {

    List<DestructionOrderDTO> findAllDestructionOrders();
    DestructionOrderDTO findDestructionOrderById(Long id);
    void deleteDestructionOrder(Long id);
    DestructionOrderDTO addDestructionOrder(DestructionOrderDTO destructionOrder);
    DestructionOrderDTO updateDestructionOrder(Long id, DestructionOrderDTO destructionOrder);
}
