package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.BuyOrderMapperImpl;
import com.manageyourtools.toolroom.catalogue.domain.UnitOfMeasure;
import com.manageyourtools.toolroom.employee.domain.EmployeeService;
import com.manageyourtools.toolroom.lending.domain.LendingOrderToolRepository;
import com.manageyourtools.toolroom.removing.domain.DestructionOrderToolRepository;
import com.manageyourtools.toolroom.catalogue.domain.Tool;
import com.manageyourtools.toolroom.catalogue.ToolRepository;
import com.manageyourtools.toolroom.warehouse.dto.OrderDto;
import com.manageyourtools.toolroom.warehouse.domain.BuyOrderRepository;
import com.manageyourtools.toolroom.warehouse.domain.BuyOrderTool;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class OrderDtoEntityServiceTest {

    @Mock
    BuyOrderRepository buyOrderRepository;

    BuyOrderService buyOrderService;
    @Mock
    EmployeeService employeeService;

    @Mock
    LendingOrderToolRepository lendingOrderToolRepository;
    @Mock
    DestructionOrderToolRepository destructionOrderToolRepository;
    @Mock
    ToolService toolService;

    @Mock
    ToolRepository toolRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        buyOrderService = new BuyOrderService(buyOrderRepository,
                employeeService, lendingOrderToolRepository, destructionOrderToolRepository, new BuyOrderMapperImpl(), toolService);
    }

    private OrderDto getBuyOrder1(){
        OrderDto orderDto = new OrderDto();
        orderDto.setAddTimestamp(Timestamp.valueOf("2018-10-10 12:22:23"));
        orderDto.setOrderCode("orderCode");
        orderDto.addBuyOrderTool(getBuyOrderTools());
        orderDto.setId(1L);

        return orderDto;
    }

    private OrderDto getBuyOrder2(){
        OrderDto orderDto = new OrderDto();
        orderDto.setAddTimestamp(Timestamp.valueOf("2018-10-10 12:22:23"));
        orderDto.setOrderCode("123");
        orderDto.addBuyOrderTool(getBuyOrderTools());
        orderDto.setId(1L);

        return orderDto;
    }

    private BuyOrderTool getBuyOrderTools(){

        BuyOrderTool buyOrderTool2 = new BuyOrderTool();
        buyOrderTool2.setId(2L);
        buyOrderTool2.setCount(3L);

        Tool screwM6 = new Tool();
        screwM6.setId(1L);
        screwM6.setAllCount(1000L);
        screwM6.setCurrentCount(1000L);
        screwM6.setIsToReturn(false);
        screwM6.setIsUnique(false);
        screwM6.setName("screw M6x30");
        screwM6.setUnitOfMeasure(UnitOfMeasure.PCS);
        screwM6.setIsEnable(false);

        screwM6.addBuyOrderTool(buyOrderTool2);

        return buyOrderTool2;
    }

    /*@Test
    public void findAllBuyOrders() {
        List<BuyOrder> buyOrders = Arrays.asList(getBuyOrder1(), getBuyOrder2());


        when(buyOrderRepository.findAll()).thenReturn(buyOrders);

        List<BuyOrderDTO> orders = buyOrderService.findAllBuyOrders();

        assertEquals(2, orders.size());
    }

    @Test
    public void findBuyOrderById() {
        BuyOrder buyOrder = getBuyOrder1();

        when(buyOrderRepository.findById(anyLong())).thenReturn(Optional.of(buyOrder));

        BuyOrderDTO order = buyOrderService.findBuyOrderById(2L);

        assertEquals(buyOrder.getOrderCode(), order.getOrderCode());
    }*/

    /*@Test(expected = IllegalArgumentException.class)
    public void uniqueToolHasAlreadyOneCopyOnWarehouse(){
        BuyOrderTool buyOrderTool = getBuyOrderTools();
        Tool catalogue = buyOrderTool.getToolId();

        buyOrderService.checkIfUniqueToolWillNotBeOverloaded(catalogue, buyOrderTool.getCount());
    }*/

    @Test
    public void correctReturnToolCounts(){
        Tool tool = getBuyOrderTools().getTool();

        buyOrderService.backToToolCountBeforePurchase(1000L, tool);

        assertEquals(0, tool.getAllCount().longValue());
        assertEquals(0, tool.getCurrentCount().longValue());
    }

    /*@Test(expected = IllegalArgumentException.class)
    public void thereIsLendingOrDestructionOperationOnToolAfterBuyOrderSoThrowException(){

    }*/

    @Test
    public void thereIsLendingOperationOnToolAfterBuyOrder(){

    }

    @Test
    public void thereIsDestructionOperationOnToolAfterBuyOrder(){

    }

    @Test
    public void correctResultOfAdditionToolCount(){
        Tool tool = getBuyOrderTools().getTool();

        buyOrderService.calculateToolCount(5L, tool);

        assertEquals(1005, tool.getAllCount().longValue());
        assertEquals(1005, tool.getCurrentCount().longValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void toolIsDisable(){
        BuyOrderTool buyOrderTool = getBuyOrderTools();
        Tool tool = buyOrderTool.getTool();

        buyOrderService.checkIfToolIsEnable(tool);
    }

    @Test
    public void decreaseToolCountIfWasInPreviousBuyOrderTest() {
        OrderDto orderDto = getBuyOrder1();
        Set<BuyOrderTool> buyOrderTools = orderDto.getBuyOrderTools();
        Tool tool = buyOrderTools.iterator().next().getTool();

        buyOrderService.decreaseToolCountIfWasInPreviousBuyOrder(buyOrderTools, tool);

        assertEquals(997, tool.getAllCount().longValue());
        assertEquals(997, tool.getCurrentCount().longValue());
    }

    /*@Test
    public void changeBuyOrderTest() {
        BuyOrder buyOrder = getBuyOrder1();
        List<BuyOrderTool> buyOrderTools = buyOrder.getBuyOrderTools();
        Tool catalogue = buyOrderTools.get(0).getToolId();

        BuyOrder newBuyOrder = buyOrder;
        newBuyOrder.getBuyOrderTools().forEach(buyOrderTool -> buyOrderTool.setCount(103L));

        buyOrderService.changeBuyOrder(buyOrder, newBuyOrder);

    }*/
}