package com.manageyourtools.toolroom.warehouse.web;

import com.manageyourtools.toolroom.warehouse.domain.OrderFacade;
import com.manageyourtools.toolroom.warehouse.domain.OrderType;
import com.manageyourtools.toolroom.warehouse.dto.OrderDto;
import com.manageyourtools.toolroom.warehouse.dto.OrderQueryDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
class OrderController {

    private final OrderFacade orderFacade;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderQueryDto getOrder(@PathVariable Long id){
        return orderFacade.find(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Set<OrderQueryDto> getOrders(){
        return orderFacade.findAll();
    }

    @PostMapping("/buy")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderQueryDto addBuyOrder(@RequestBody OrderDto orderDto){
        return orderFacade.addOrder(orderDto, OrderType.SUPPLY);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderQueryDto updateOrder(@RequestBody OrderDto orderDto, @PathVariable Long id) {
        if (!id.equals(orderDto.getId())) {
            throw new IllegalArgumentException("ID's do not matches");
        }
        return orderFacade.update(orderDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteOrder(@PathVariable Long id){
        orderFacade.delete(id);
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderQueryDto addRemoveOrder(@RequestBody @Valid OrderDto orderDto){
        return orderFacade.addOrder(orderDto, OrderType.REMOVE);
    }
}
