package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.controllers.assembler.LendingOrderResourceAssembler;
import com.manageyourtools.toolroom.domains.LendingOrder;
import com.manageyourtools.toolroom.services.LendingOrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/lendingorders")
public class LendingOrderController {

    private final LendingOrderService lendingOrderService;
    private final LendingOrderResourceAssembler assembler;

    public LendingOrderController(LendingOrderService lendingOrderService, LendingOrderResourceAssembler assembler) {
        this.lendingOrderService = lendingOrderService;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<LendingOrder> getLendingOrder(@PathVariable Long id){

        LendingOrder lendingOrder = lendingOrderService.findLendingOrderById(id);

        return assembler.toResource(lendingOrder);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PagedResources<Resource<LendingOrder>> getLendingOrders(Pageable pageable, PagedResourcesAssembler<LendingOrder> pagedResourcesAssembler){

        return pagedResourcesAssembler.toResource(
                lendingOrderService.findAllLendingOrders(pageable),
                assembler,
                linkTo(methodOn(LendingOrderController.class).getLendingOrders(pageable,pagedResourcesAssembler)).withSelfRel());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<LendingOrder> addLendingOrder(@RequestBody LendingOrder lendingOrder){

        return assembler.toResource(lendingOrderService.addLendingOrder(lendingOrder));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<LendingOrder> updateLendingOrder(@RequestBody LendingOrder lendingOrder, @PathVariable Long id) {

        return assembler.toResource(lendingOrderService.updateLendingOrder(id, lendingOrder));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLendingOrder(@PathVariable Long id){

        lendingOrderService.deleteLendingOrder(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<LendingOrder> patchLendingOrder(@PathVariable Long id, @RequestBody LendingOrder lendingOrder) {

        return assembler.toResource(lendingOrderService.patchLendingOrder(id, lendingOrder));
    }
}
