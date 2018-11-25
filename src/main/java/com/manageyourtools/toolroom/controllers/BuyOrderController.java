package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.controllers.assembler.BuyOrderResourceAssembler;
import com.manageyourtools.toolroom.domains.BuyOrder;
import com.manageyourtools.toolroom.services.BuyOrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/buy")
public class BuyOrderController {

    private final BuyOrderService buyOrderService;

    private final BuyOrderResourceAssembler assembler;

    public BuyOrderController(BuyOrderService buyOrderService, BuyOrderResourceAssembler assembler) {
        this.buyOrderService = buyOrderService;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<BuyOrder> getBought(@PathVariable Long id){

        BuyOrder buyOrder = buyOrderService.findBoughtById(id);

        return assembler.toResource(buyOrder);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PagedResources<Resource<BuyOrder>> getBoughts(Pageable pageable, PagedResourcesAssembler<BuyOrder> pagedResourcesAssembler){


        return pagedResourcesAssembler.toResource(
                buyOrderService.findAllBoughts(pageable),
                assembler,
                linkTo(methodOn(BuyOrderController.class).getBoughts(pageable,pagedResourcesAssembler)).withSelfRel());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<BuyOrder> addTool(@RequestBody BuyOrder buyOrder){

        return assembler.toResource(buyOrderService.addBought(buyOrder));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<BuyOrder> updateBought(@RequestBody BuyOrder buyOrder, @PathVariable Long id) {

        return assembler.toResource(buyOrderService.updateBought(id, buyOrder));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBought(@PathVariable Long id){

        buyOrderService.deleteBought(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<BuyOrder> patchTool(@PathVariable Long id, @RequestBody BuyOrder buyOrder) {

        return assembler.toResource(buyOrderService.patchBought(id, buyOrder));
    }

}
