package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.LendingReturnOrderDTO;
import com.manageyourtools.toolroom.services.LendingReturnOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LendingReturnOrderController {

    private final LendingReturnOrderService lendingReturnOrderService;

    public LendingReturnOrderController(LendingReturnOrderService lendingReturnOrderService) {
        this.lendingReturnOrderService = lendingReturnOrderService;
    }

    @PutMapping("/lendingreturnorders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LendingReturnOrderDTO returnLendingOrder(@PathVariable Long id, @RequestBody LendingReturnOrderDTO lendingReturnOrderDTO) {
        return lendingReturnOrderService.returnLendingOrder(id, lendingReturnOrderDTO);
    }

    @GetMapping("/lendingreturnorders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LendingReturnOrderDTO getLendingReturnOrder(@PathVariable Long id) {
        return lendingReturnOrderService.getLendingReturnOrderDTO(id);
    }

    @GetMapping("/lendingreturnorders")
    @ResponseStatus(HttpStatus.OK)
    public List<LendingReturnOrderDTO> getLendingReturnOrders() {
        return lendingReturnOrderService.getAllOrders();
    }
}
