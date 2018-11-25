package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.BuyOrder;
import com.manageyourtools.toolroom.repositories.BuyOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BuyOrderServiceImpl implements BuyOrderService {

    private BuyOrderRepository buyOrderRepository;

    public BuyOrderServiceImpl(BuyOrderRepository buyOrderRepository) {
        this.buyOrderRepository = buyOrderRepository;
    }

    @Override
    public Page<BuyOrder> findAllBoughts(Pageable pageable) {

        return buyOrderRepository.findAll(pageable);
    }

    @Override
    public BuyOrder findBoughtById(Long id) {
        return buyOrderRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public void deleteBought(Long id) {
        //todo not allow to delete bought if there were any destroyed tool or picked up tool after date of addition this bought
        buyOrderRepository.deleteById(id);
    }

    @Override
    public BuyOrder addBought(BuyOrder buyOrder) {
        return buyOrderRepository.save(buyOrder);
    }

    @Override
    public BuyOrder updateBought(Long id, BuyOrder buyOrder) {
        //todo not allow to update bought if there were any destroyed tool or picked up tool after date of addition this bought
        buyOrder.setId(id);
        return buyOrderRepository.save(buyOrder);
    }

    @Override
    public BuyOrder patchBought(Long id, BuyOrder buyOrder) {
        //todo I'm not sure if patch method will be necessary here
        return null;
    }
}
