package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.mapper.BuyOrderMapper;
import com.manageyourtools.toolroom.api.model.BuyOrderDTO;
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
@RequestMapping("/buyorders")
public class BuyOrderController {

    private final BuyOrderService buyOrderService;
    private final BuyOrderResourceAssembler assembler;

    public BuyOrderController(BuyOrderService buyOrderService, BuyOrderResourceAssembler assembler) {
        this.buyOrderService = buyOrderService;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<BuyOrderDTO> getBuyOrder(@PathVariable Long id){

        BuyOrderDTO buyOrderDTO = buyOrderService.findBuyOrderById(id);

        return assembler.toResource(buyOrderDTO);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PagedResources<Resource<BuyOrderDTO>> getBuyOrders(Pageable pageable, PagedResourcesAssembler<BuyOrderDTO> pagedResourcesAssembler){

        return pagedResourcesAssembler.toResource(
                buyOrderService.findAllBuyOrders(pageable),
                assembler,
                linkTo(methodOn(BuyOrderController.class).getBuyOrders(pageable,pagedResourcesAssembler)).withSelfRel());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<BuyOrderDTO> addBuyOrder(@RequestBody BuyOrderDTO buyOrderDTO){

        return assembler.toResource(buyOrderService.addBuyOrder(buyOrderDTO));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<BuyOrderDTO> updateBuyOrder(@RequestBody BuyOrderDTO buyOrderDTO, @PathVariable Long id) {

        return assembler.toResource(buyOrderService.updateBuyOrder(id, buyOrderDTO));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBuyOrder(@PathVariable Long id){

        buyOrderService.deleteBuyOrder(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<BuyOrderDTO> patchBuyOrder(@PathVariable Long id, @RequestBody BuyOrderDTO buyOrderDTO) {

        return assembler.toResource(buyOrderService.patchBuyOrder(id, buyOrderDTO));
    }

}
