package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.domains.LendingOrder;
import com.manageyourtools.toolroom.services.LendingOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/lendingorders")
public class LendingOrderController {

    private final LendingOrderService lendingOrderService;

    public LendingOrderController(LendingOrderService lendingOrderService) {
        this.lendingOrderService = lendingOrderService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LendingOrder getLendingOrder(@PathVariable Long id){

        return lendingOrderService.findLendingOrderById(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Page<LendingOrder> getLendingOrders(Pageable pageable){

        return lendingOrderService.findAllLendingOrders(pageable);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public LendingOrder addLendingOrder(@RequestBody LendingOrder lendingOrder){

        return lendingOrderService.addLendingOrder(lendingOrder);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public LendingOrder updateLendingOrder(@RequestBody LendingOrder lendingOrder, @PathVariable Long id) {

        return lendingOrderService.updateLendingOrder(id, lendingOrder);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLendingOrder(@PathVariable Long id){

        lendingOrderService.deleteLendingOrder(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LendingOrder patchLendingOrder(@PathVariable Long id, @RequestBody LendingOrder lendingOrder) {

        return lendingOrderService.patchLendingOrder(id, lendingOrder);
    }
}
