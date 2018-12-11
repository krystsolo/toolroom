package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.LendingOrder;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.LendingOrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.manageyourtools.toolroom.services.UserDetailsServiceImpl.HAS_ROLE_WAREHOUSEMAN;

@Service
public class LendingOrderServiceImpl implements LendingOrderService {

    private final LendingOrderRepository lendingOrderRepository;

    public LendingOrderServiceImpl(LendingOrderRepository lendingOrderRepository) {
        this.lendingOrderRepository = lendingOrderRepository;
    }

    @Override
    public List<LendingOrder> findAllLendingOrders() {
        return lendingOrderRepository.findAll();
    }

    @Override
    public LendingOrder findLendingOrderById(Long id) {
        return lendingOrderRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public void deleteLendingOrder(Long id) {
        lendingOrderRepository.deleteById(id);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public LendingOrder addLendingOrder(LendingOrder lendingOrder) {
        return lendingOrderRepository.save(lendingOrder);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public LendingOrder updateLendingOrder(Long id, LendingOrder lendingOrder) {
        lendingOrder.setId(id);
        return lendingOrderRepository.save(lendingOrder);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public LendingOrder patchLendingOrder(Long id, LendingOrder lendingOrder) {
        return null;
    }
}
