package com.manageyourtools.toolroom.warehouse.domain;

import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.Set;

interface OrderRepository extends Repository<Order, Long> {
    Order save(Order order);
    Optional<Order> findById(Long id);
    Set<Order> findAll();
    void delete(Long id);

    default Order getOrderOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResourceNotFoundException("Order with id=" +  id + " not found"));
    }
}
