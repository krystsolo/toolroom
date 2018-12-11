package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.domains.DestructionOrder;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.DestructionOrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.manageyourtools.toolroom.services.UserDetailsServiceImpl.HAS_ROLE_WAREHOUSEMAN;

@Service
public class DestructionOrderServiceImpl implements DestructionOrderService {

    private final DestructionOrderRepository destructionOrderRepository;

    public DestructionOrderServiceImpl(DestructionOrderRepository destructionOrderRepository) {
        this.destructionOrderRepository = destructionOrderRepository;
    }

    @Override
    public List<DestructionOrder> findAllDestructionOrders() {
        return destructionOrderRepository.findAll();
    }

    @Override
    public DestructionOrder findDestructionOrderById(Long id) {
        return destructionOrderRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public void deleteDestructionOrder(Long id) {
        //todo retrieve tools which status were changed to destroyed - tools and their counts, change tool status 'isEnable' to true
        destructionOrderRepository.deleteById(id);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public DestructionOrder addDestructionOrder(DestructionOrder destructionOrder) {
        //todo if tool has unique flag and count on magazine is equal 1 then change currentCount and maxCOunt to 0 and set isEnable to false
        //if tool count to destroy is bigger then current count then throw not enough tools count to order them as destroyed exception
        //if tool is not unique then decrease count of tool - if allCount equal 0 then set isEnable to false
        return destructionOrderRepository.save(destructionOrder);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public DestructionOrder updateDestructionOrder(Long id, DestructionOrder destructionOrder) {
        //todo check if changed number of tools is bigger from this before or smaller
        //-> if smaller than retrieve specified number of tool and check if there is need to change isEnable status
        //-> if bigger than change number of specified tool is not bigger than current value of Tool on magazine -> throw exception or change number of max and current count and isEnable if needed
        destructionOrder.setId(id);
        return destructionOrderRepository.save(destructionOrder);
    }

    @Override
    @PreAuthorize(HAS_ROLE_WAREHOUSEMAN)
    public DestructionOrder patchDestructionOrder(Long id, DestructionOrder destructionOrder) {
        return null;
    }
}
