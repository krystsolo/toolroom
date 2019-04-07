package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.DestructionOrderDTO;
import com.manageyourtools.toolroom.domains.DestructionOrder;
import com.manageyourtools.toolroom.services.DestructionOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/destructionorders")
public class DestructionOrderController {

    private final DestructionOrderService destructionOrderService;


    public DestructionOrderController(DestructionOrderService destructionOrderService) {
        this.destructionOrderService = destructionOrderService;
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DestructionOrderDTO getDestructionOrder(@PathVariable Long id){

        return destructionOrderService.findDestructionOrderById(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<DestructionOrderDTO> getDestructionOrders(){

        return destructionOrderService.findAllDestructionOrders();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public DestructionOrderDTO addDestructionOrder(@RequestBody DestructionOrderDTO destructionOrder){

        return destructionOrderService.addDestructionOrder(destructionOrder);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public DestructionOrderDTO updateDestructionOrder(@RequestBody DestructionOrderDTO destructionOrder, @PathVariable Long id) {

        return destructionOrderService.updateDestructionOrder(id, destructionOrder);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDestructionOrder(@PathVariable Long id){

        destructionOrderService.deleteDestructionOrder(id);
    }
}
