package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.LendingReturnOrderDTO;
import com.manageyourtools.toolroom.services.LendingReturnOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lendingreturnorders")
public class LendingReturnOrderController {

    private final LendingReturnOrderService lendingReturnOrderService;

    public LendingReturnOrderController(LendingReturnOrderService lendingReturnOrderService) {
        this.lendingReturnOrderService = lendingReturnOrderService;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LendingReturnOrderDTO returnLendingOrder(@PathVariable Long id, @RequestBody LendingReturnOrderDTO lendingReturnOrderDTO) {
        return lendingReturnOrderService.returnLendingOrder(id, lendingReturnOrderDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LendingReturnOrderDTO getLendingReturnOrder(@PathVariable Long id) {
        return lendingReturnOrderService.getLendingReturnOrderDTO(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<LendingReturnOrderDTO> getLendingReturnOrders(
            @RequestParam(defaultValue = "false", required = false) boolean lateNotReturned) {

        if (lateNotReturned) {
            return lendingReturnOrderService.getAllOrdersNotReturnedOnTime();
        }
        return lendingReturnOrderService.getAllOrders();
    }
}
