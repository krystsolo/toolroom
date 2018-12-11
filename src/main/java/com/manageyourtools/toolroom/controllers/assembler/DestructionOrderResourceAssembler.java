package com.manageyourtools.toolroom.controllers.assembler;

import com.manageyourtools.toolroom.controllers.DestructionOrderController;
import com.manageyourtools.toolroom.domains.DestructionOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class DestructionOrderResourceAssembler implements ResourceAssembler<DestructionOrder, Resource<DestructionOrder>> {
    @Override
    public Resource<DestructionOrder> toResource(DestructionOrder destructionOrder) {
        return new Resource<>(destructionOrder,
                ControllerLinkBuilder.linkTo(methodOn(DestructionOrderController.class).getDestructionOrder(destructionOrder.getId())).withSelfRel(),
                linkTo(methodOn(DestructionOrderController.class).getDestructionOrders(Pageable.unpaged(),null)).withRel("destructionOrders"));
    }
}
