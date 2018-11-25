package com.manageyourtools.toolroom.controllers.assembler;

import com.manageyourtools.toolroom.controllers.BuyOrderController;
import com.manageyourtools.toolroom.domains.BuyOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class BuyOrderResourceAssembler implements ResourceAssembler<BuyOrder, Resource<BuyOrder>> {

    @Override
    public Resource<BuyOrder> toResource(BuyOrder buyOrder) {

        return new Resource<>(buyOrder,
                ControllerLinkBuilder.linkTo(methodOn(BuyOrderController.class).getBought(buyOrder.getId())).withSelfRel(),
                linkTo(methodOn(BuyOrderController.class).getBoughts(Pageable.unpaged(),null)).withRel("buyOrders"));
    }
}