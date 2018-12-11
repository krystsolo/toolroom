package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.model.BuyOrderDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface BuyOrderService {

    List<BuyOrderDTO> findAllBuyOrders();
    BuyOrderDTO findBuyOrderById(Long id);
    void deleteBuyOrder(Long id);
    BuyOrderDTO addBuyOrder(BuyOrderDTO buyOrderDTO) throws IllegalArgumentException;
    BuyOrderDTO updateBuyOrder(Long id, BuyOrderDTO buyOrderDTO);
    BuyOrderDTO patchBuyOrder(Long id, BuyOrderDTO buyOrderDTO);
}
