package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.LendingOrderDTO;

import com.manageyourtools.toolroom.services.LendingOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/lendingorders")
public class LendingOrderController {

    private final LendingOrderService lendingOrderService;

    public LendingOrderController(LendingOrderService lendingOrderService) {
        this.lendingOrderService = lendingOrderService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LendingOrderDTO getLendingOrder(@PathVariable Long id){

        return lendingOrderService.findLendingOrderDTOById(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<LendingOrderDTO> getLendingOrders(){

        return lendingOrderService.findAllLendingOrders();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public LendingOrderDTO addLendingOrder(@RequestBody LendingOrderDTO lendingOrder){

        return lendingOrderService.addLendingOrder(lendingOrder);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LendingOrderDTO updateLendingOrder(@RequestBody LendingOrderDTO lendingOrder, @PathVariable Long id) {

        return lendingOrderService.updateLendingOrder(id, lendingOrder);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLendingOrder(@PathVariable Long id){

        lendingOrderService.deleteLendingOrder(id);
    }
}
