package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.DestructionOrder;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DestructionOrderService {

    List<DestructionOrder> findAllDestructionOrders();
    DestructionOrder findDestructionOrderById(Long id);
    void deleteDestructionOrder(Long id);
    DestructionOrder addDestructionOrder(DestructionOrder destructionOrder);
    DestructionOrder updateDestructionOrder(Long id, DestructionOrder destructionOrder);
    DestructionOrder patchDestructionOrder(Long id, DestructionOrder destructionOrder);
}
