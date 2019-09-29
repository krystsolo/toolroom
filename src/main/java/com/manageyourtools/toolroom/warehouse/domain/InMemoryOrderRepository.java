package com.manageyourtools.toolroom.warehouse.domain;

import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
class InMemoryOrderRepository implements OrderRepository {
    private final Map<Long, Order> db = new HashMap<>();

    @Override
    public Order save(Order order) {
        return db.put(order.toDto().getId(), order);
    }
    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }
    @Override
    public Set<Order> findAll() {
        return new HashSet<>(db.values());
    }
    @Override
    public void delete(Long id) {
        db.remove(id);
    }
}
