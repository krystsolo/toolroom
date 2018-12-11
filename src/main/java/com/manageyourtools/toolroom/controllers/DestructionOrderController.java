package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.controllers.assembler.DestructionOrderResourceAssembler;
import com.manageyourtools.toolroom.domains.DestructionOrder;
import com.manageyourtools.toolroom.services.DestructionOrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/destructionorders")
public class DestructionOrderController {

    private final DestructionOrderService destructionOrderService;
    private final DestructionOrderResourceAssembler assembler;

    public DestructionOrderController(DestructionOrderService destructionOrderService, DestructionOrderResourceAssembler assembler) {
        this.destructionOrderService = destructionOrderService;
        this.assembler = assembler;
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<DestructionOrder> getDestructionOrder(@PathVariable Long id){

        DestructionOrder destructionOrder = destructionOrderService.findDestructionOrderById(id);

        return assembler.toResource(destructionOrder);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PagedResources<Resource<DestructionOrder>> getDestructionOrders(Pageable pageable, PagedResourcesAssembler<DestructionOrder> pagedResourcesAssembler){

        return pagedResourcesAssembler.toResource(
               destructionOrderService.findAllDestructionOrders(pageable),
                assembler,
                linkTo(methodOn(DestructionOrderController.class).getDestructionOrders(pageable,pagedResourcesAssembler)).withSelfRel());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<DestructionOrder> addDestructionOrder(@RequestBody DestructionOrder destructionOrder){

        return assembler.toResource(destructionOrderService.addDestructionOrder(destructionOrder));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<DestructionOrder> updateDestructionOrder(@RequestBody DestructionOrder destructionOrder, @PathVariable Long id) {

        return assembler.toResource(destructionOrderService.updateDestructionOrder(id, destructionOrder));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDestructionOrder(@PathVariable Long id){

        destructionOrderService.deleteDestructionOrder(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<DestructionOrder> patchDestructionOrder(@PathVariable Long id, @RequestBody DestructionOrder destructionOrder) {

        return assembler.toResource(destructionOrderService.patchDestructionOrder(id, destructionOrder));
    }
}
