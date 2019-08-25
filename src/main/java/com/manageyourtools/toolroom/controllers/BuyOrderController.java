package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.BuyOrderDTO;
import com.manageyourtools.toolroom.services.BuyOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buyorders")
public class BuyOrderController {

    private final BuyOrderService buyOrderService;

    public BuyOrderController(BuyOrderService buyOrderService) {
        this.buyOrderService = buyOrderService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BuyOrderDTO getBuyOrder(@PathVariable Long id){

        return buyOrderService.findBuyOrderById(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<BuyOrderDTO> getBuyOrders(){

        return buyOrderService.findAllBuyOrders();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public BuyOrderDTO addBuyOrder(@RequestBody BuyOrderDTO buyOrderDTO){

        return buyOrderService.addBuyOrder(buyOrderDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public BuyOrderDTO updateBuyOrder(@RequestBody BuyOrderDTO buyOrderDTO, @PathVariable Long id) {

        return buyOrderService.updateBuyOrder(id, buyOrderDTO);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBuyOrder(@PathVariable Long id){

        buyOrderService.deleteBuyOrder(id);
    }
}
