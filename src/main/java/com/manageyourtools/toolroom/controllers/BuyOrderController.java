package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.BuyOrderDTO;
import com.manageyourtools.toolroom.services.BuyOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public Page<BuyOrderDTO> getBuyOrders(Pageable pageable){

        return buyOrderService.findAllBuyOrders(pageable);
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

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BuyOrderDTO patchBuyOrder(@PathVariable Long id, @RequestBody BuyOrderDTO buyOrderDTO) {

        return buyOrderService.patchBuyOrder(id, buyOrderDTO);
    }

}
