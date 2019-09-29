package com.manageyourtools.toolroom.warehouse.domain

import com.manageyourtools.toolroom.catalogue.domain.CatalogueFacade
import com.manageyourtools.toolroom.catalogue.dto.ToolAllQuantityChange
import com.manageyourtools.toolroom.infrastructure.config.AuthenticationFacade
import com.manageyourtools.toolroom.warehouse.dto.OrderDto
import com.manageyourtools.toolroom.warehouse.dto.OrderToolDto
import spock.lang.Specification

class OrderFacadeSpec extends Specification {

    AuthenticationFacade authenticationFacade = Mock()
    CatalogueFacade catalogueFacade = Mock()
    OrderFacade orderFacade = new OrderConfiguration().orderFacade(authenticationFacade, catalogueFacade)

    def "should show order"() {
        given: "there is order in db"
        def orderId = 1
        def toolId = 2
        def count = 2
        OrderToolDto orderToolDto = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto orderDto = OrderDto.builder().id(orderId).orderTools([orderToolDto].toSet()).build()
        orderFacade.addOrder(orderDto, OrderType.SUPPLY)

        expect:
        orderFacade.find(orderId)
    }

    def "should show all orders"() {
        given: "there is orders in db"
        OrderDto order1 = OrderDto.builder().id(1).build()
        OrderDto order2 = OrderDto.builder().id(2).build()
        orderFacade.addOrder(order1, OrderType.SUPPLY)
        orderFacade.addOrder(order2, OrderType.REMOVE)

        expect:
        orderFacade.findAll().size() == 2
    }

    def "should delete order"() {
        given: "there is order in db"
        def orderId = 1
        def toolId = 2
        def count = 2
        OrderToolDto orderToolDto = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto orderDto = OrderDto.builder().id(orderId).orderTools([orderToolDto].toSet()).build()
        orderFacade.addOrder(orderDto, OrderType.SUPPLY)

        when:
        orderFacade.delete(orderId)

        then:
        orderFacade.findAll().size() == 0
    }

    def "should save buy order"() {
        given:
        def orderId = 1
        def toolId = 2
        def count = 2
        OrderToolDto orderToolDto = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto orderDto = OrderDto.builder().id(orderId).orderTools([orderToolDto].toSet()).build()

        when:
        orderFacade.addOrder(orderDto, OrderType.SUPPLY)

        then:
        orderFacade.find(orderId)
        1 * catalogueFacade.calculateToolAllQuantity(new ToolAllQuantityChange(toolId, count))
    }

    def "should save removing order"() {
        given:
        def orderId = 1
        def toolId = 2
        def count = 2
        OrderToolDto orderToolDto = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto orderDto = OrderDto.builder().id(orderId).orderTools([orderToolDto].toSet()).build()

        when:
        orderFacade.addOrder(orderDto, OrderType.REMOVE)

        then:
        1 * catalogueFacade.calculateToolAllQuantity(new ToolAllQuantityChange(toolId, -1 * count))
        orderFacade.find(orderId)
    }

    def "should update buy order if quantity in the order was decreased for tool"() {
        given: "there is buy order in db"
        def orderId = 1
        def toolId = 2
        def count = 10
        def newCount = 5
        OrderToolDto orderToolInDb = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto orderInDb = OrderDto.builder().id(orderId).orderTools([orderToolInDb].toSet()).build()
        orderFacade.addOrder(orderInDb, OrderType.SUPPLY)

        OrderToolDto updateOrderTool = OrderToolDto.builder().id(1).toolId(toolId).count(newCount).build()
        OrderDto updateOrder = OrderDto.builder().id(orderId).orderTools([updateOrderTool].toSet()).build()

        when:
        def orderQueryDto = orderFacade.update(updateOrder)

        then:
        1 * catalogueFacade.calculateToolAllQuantity(new ToolAllQuantityChange(toolId, newCount - count))
        orderQueryDto.orderTools.size() == 1
        orderQueryDto.orderTools.first().count == newCount
    }

    def "should update buy order if quantity in the order was increased for tool"() {
        given: "there is buy order in db"
        def orderId = 1
        def toolId = 2
        def count = 5
        def newCount = 10
        OrderToolDto orderToolInDb = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto orderInDb = OrderDto.builder().id(orderId).orderTools([orderToolInDb].toSet()).build()
        orderFacade.addOrder(orderInDb, OrderType.SUPPLY)

        OrderToolDto updateOrderTool = OrderToolDto.builder().id(1).toolId(toolId).count(newCount).build()
        OrderDto updateOrder = OrderDto.builder().id(orderId).orderTools([updateOrderTool].toSet()).build()

        when:
        def orderQueryDto = orderFacade.update(updateOrder)

        then:
        1 * catalogueFacade.calculateToolAllQuantity(new ToolAllQuantityChange(toolId, newCount - count))
        orderQueryDto.orderTools.size() == 1
        orderQueryDto.orderTools.first().count == newCount
    }

    def "should update buy order if tool was deleted from the order"() {
        given: "there is buy order in db"
        def orderId = 1
        def toolId = 2
        def count = 5
        OrderToolDto orderToolInDb = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto orderInDb = OrderDto.builder().id(orderId).orderTools([orderToolInDb].toSet()).build()
        orderFacade.addOrder(orderInDb, OrderType.SUPPLY)

        OrderDto updateOrder = OrderDto.builder().id(orderId).build()

        when:
        def orderQueryDto = orderFacade.update(updateOrder)

        then:
        1 * catalogueFacade.calculateToolAllQuantity(new ToolAllQuantityChange(toolId, - count))
        orderQueryDto.orderTools.size() == 0
    }

    def "should update buy order if tool was added to the order"() {
        given: "there is buy order in db"
        def orderId = 1
        def toolId = 2
        def count = 5
        OrderDto orderInDb = OrderDto.builder().id(orderId).build()
        orderFacade.addOrder(orderInDb, OrderType.SUPPLY)

        OrderToolDto updateOrderTool = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto updateOrder = OrderDto.builder().id(orderId).orderTools([updateOrderTool].toSet()).build()

        when:
        def orderQueryDto = orderFacade.update(updateOrder)

        then:
        1 * catalogueFacade.calculateToolAllQuantity(new ToolAllQuantityChange(toolId, count))
        orderQueryDto.orderTools.size() == 1
        orderQueryDto.orderTools.first().count == count
    }

    def "should update buy order if quantity for tool in order was not changed"() {
        given: "there is buy order in db"
        def orderId = 1
        def toolId = 2
        def count = 10
        OrderToolDto orderToolInDb = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto orderInDb = OrderDto.builder().id(orderId).orderTools([orderToolInDb].toSet()).build()
        orderFacade.addOrder(orderInDb, OrderType.SUPPLY)

        when:
        def orderQueryDto = orderFacade.update(orderInDb)

        then:
        0 * catalogueFacade.calculateToolAllQuantity()
        orderQueryDto.orderTools.size() == 1
        orderQueryDto.orderTools.first().count == count
    }

    def "should update remove order if quantity in the order was decreased for tool"() {
        given: "there is remove order in db"
        def orderId = 1
        def toolId = 2
        def count = 10
        def newCount = 5
        OrderToolDto orderToolInDb = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto orderInDb = OrderDto.builder().id(orderId).orderTools([orderToolInDb].toSet()).build()
        orderFacade.addOrder(orderInDb, OrderType.REMOVE)

        OrderToolDto updateOrderTool = OrderToolDto.builder().id(1).toolId(toolId).count(newCount).build()
        OrderDto updateOrder = OrderDto.builder().id(orderId).orderTools([updateOrderTool].toSet()).build()

        when:
        def orderQueryDto = orderFacade.update(updateOrder)

        then:
        1 * catalogueFacade.calculateToolAllQuantity(new ToolAllQuantityChange(toolId, count - newCount))
        orderQueryDto.orderTools.size() == 1
        orderQueryDto.orderTools.first().count == newCount
    }

    def "should update remove order if quantity in the order was increased for tool"() {
        given: "there is remove order in db"
        def orderId = 1
        def toolId = 2
        def count = 5
        def newCount = 10
        OrderToolDto orderToolInDb = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto orderInDb = OrderDto.builder().id(orderId).orderTools([orderToolInDb].toSet()).build()
        orderFacade.addOrder(orderInDb, OrderType.REMOVE)

        OrderToolDto updateOrderTool = OrderToolDto.builder().id(1).toolId(toolId).count(newCount).build()
        OrderDto updateOrder = OrderDto.builder().id(orderId).orderTools([updateOrderTool].toSet()).build()

        when:
        def orderQueryDto = orderFacade.update(updateOrder)

        then:
        1 * catalogueFacade.calculateToolAllQuantity(new ToolAllQuantityChange(toolId, count - newCount))
        orderQueryDto.orderTools.size() == 1
        orderQueryDto.orderTools.first().count == newCount
    }

    def "should update remove order if tool was deleted from the order"() {
        given: "there is remove order in db"
        def orderId = 1
        def toolId = 2
        def count = 5
        OrderToolDto orderToolInDb = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto orderInDb = OrderDto.builder().id(orderId).orderTools([orderToolInDb].toSet()).build()
        orderFacade.addOrder(orderInDb, OrderType.REMOVE)

        OrderDto updateOrder = OrderDto.builder().id(orderId).build()

        when:
        def orderQueryDto = orderFacade.update(updateOrder)

        then:
        1 * catalogueFacade.calculateToolAllQuantity(new ToolAllQuantityChange(toolId, count))
        orderQueryDto.orderTools.size() == 0
    }

    def "should update remove order if tool was added to the order"() {
        given: "there is remove order in db"
        def orderId = 1
        def toolId = 2
        def count = 5
        OrderDto orderInDb = OrderDto.builder().id(orderId).build()
        orderFacade.addOrder(orderInDb, OrderType.REMOVE)

        OrderToolDto updateOrderTool = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto updateOrder = OrderDto.builder().id(orderId).orderTools([updateOrderTool].toSet()).build()

        when:
        def orderQueryDto = orderFacade.update(updateOrder)

        then:
        1 * catalogueFacade.calculateToolAllQuantity(new ToolAllQuantityChange(toolId, - count))
        orderQueryDto.orderTools.size() == 1
        orderQueryDto.orderTools.first().count == count
    }

    def "should update remove order if quantity for tool in order was not changed"() {
        given: "there is remove order in db"
        def orderId = 1
        def toolId = 2
        def count = 10
        OrderToolDto orderToolInDb = OrderToolDto.builder().id(1).toolId(toolId).count(count).build()
        OrderDto orderInDb = OrderDto.builder().id(orderId).orderTools([orderToolInDb].toSet()).build()
        orderFacade.addOrder(orderInDb, OrderType.REMOVE)

        when:
        def orderQueryDto = orderFacade.update(orderInDb)

        then:
        0 * catalogueFacade.calculateToolAllQuantity()
        orderQueryDto.orderTools.size() == 1
        orderQueryDto.orderTools.first().count == count
    }
}
