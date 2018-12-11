package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.domains.DestructionOrder;
import com.manageyourtools.toolroom.services.DestructionOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/destructionorders")
public class DestructionOrderController {

    private final DestructionOrderService destructionOrderService;


    public DestructionOrderController(DestructionOrderService destructionOrderService) {
        this.destructionOrderService = destructionOrderService;
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DestructionOrder getDestructionOrder(@PathVariable Long id){

        return destructionOrderService.findDestructionOrderById(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Page<DestructionOrder> getDestructionOrders(Pageable pageable){

        return destructionOrderService.findAllDestructionOrders(pageable);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public DestructionOrder addDestructionOrder(@RequestBody DestructionOrder destructionOrder){

        return destructionOrderService.addDestructionOrder(destructionOrder);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public DestructionOrder updateDestructionOrder(@RequestBody DestructionOrder destructionOrder, @PathVariable Long id) {

        return destructionOrderService.updateDestructionOrder(id, destructionOrder);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDestructionOrder(@PathVariable Long id){

        destructionOrderService.deleteDestructionOrder(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DestructionOrder patchDestructionOrder(@PathVariable Long id, @RequestBody DestructionOrder destructionOrder) {

        return destructionOrderService.patchDestructionOrder(id, destructionOrder);
    }
}