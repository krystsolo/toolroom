package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.model.BuyOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BuyOrderService {

    Page<BuyOrderDTO> findAllBuyOrders(Pageable pageable);
    BuyOrderDTO findBuyOrderById(Long id);
    void deleteBuyOrder(Long id);
    BuyOrderDTO addBuyOrder(BuyOrderDTO buyOrderDTO) throws IllegalArgumentException;
    BuyOrderDTO updateBuyOrder(Long id, BuyOrderDTO buyOrderDTO);
    BuyOrderDTO patchBuyOrder(Long id, BuyOrderDTO buyOrderDTO);
}
