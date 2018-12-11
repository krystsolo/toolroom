package com.manageyourtools.toolroom.controllers.assembler;

import com.manageyourtools.toolroom.controllers.LendingOrderController;
import com.manageyourtools.toolroom.domains.LendingOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class LendingOrderResourceAssembler implements ResourceAssembler<LendingOrder, Resource<LendingOrder>> {

    @Override
    public Resource<LendingOrder> toResource(LendingOrder lendingOrder) {
        return new Resource<>(lendingOrder,
                ControllerLinkBuilder.linkTo(methodOn(LendingOrderController.class).getLendingOrder(lendingOrder.getId())).withSelfRel(),
                linkTo(methodOn(LendingOrderController.class).getLendingOrders(Pageable.unpaged(),null)).withRel("lendingOrders"));
    }
}
