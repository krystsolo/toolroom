package com.manageyourtools.toolroom.controllers.assembler;

import com.manageyourtools.toolroom.api.model.BuyOrderDTO;
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
public class BuyOrderResourceAssembler implements ResourceAssembler<BuyOrderDTO, Resource<BuyOrderDTO>> {

    @Override
    public Resource<BuyOrderDTO> toResource(BuyOrderDTO buyOrderDTO) {

        return new Resource<>(buyOrderDTO,
                ControllerLinkBuilder.linkTo(methodOn(BuyOrderController.class).getBuyOrder(buyOrderDTO.getId())).withSelfRel(),
                linkTo(methodOn(BuyOrderController.class).getBuyOrders(Pageable.unpaged(),null)).withRel("buyOrders"));
    }
}