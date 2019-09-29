package com.manageyourtools.toolroom.warehouse.domain;

import com.manageyourtools.toolroom.catalogue.domain.CatalogueFacade;
import com.manageyourtools.toolroom.infrastructure.config.AuthenticationFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OrderConfiguration {

    @Bean
    OrderFacade orderFacade(OrderRepository orderRepository, AuthenticationFacade authenticationFacade, CatalogueFacade catalogueFacade) {
        OrderFactory orderFactory = new OrderFactory(orderRepository);
        return new OrderFacade(orderRepository, orderFactory, authenticationFacade, catalogueFacade);
    }

    OrderFacade orderFacade(AuthenticationFacade authenticationFacade, CatalogueFacade catalogueFacade) {
        OrderRepository orderRepository = new InMemoryOrderRepository();
        OrderFactory orderFactory = new OrderFactory(orderRepository);
        return new OrderFacade(orderRepository, orderFactory, authenticationFacade, catalogueFacade);
    }
}
