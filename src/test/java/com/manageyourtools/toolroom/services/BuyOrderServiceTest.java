package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.BuyOrderMapperImpl;
import com.manageyourtools.toolroom.config.AuthenticationFacadeImpl;
import com.manageyourtools.toolroom.domains.*;
import com.manageyourtools.toolroom.repositories.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class BuyOrderServiceTest {

    @Mock
    BuyOrderRepository buyOrderRepository;

    BuyOrderServiceImpl buyOrderService;
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

        buyOrderService = new BuyOrderServiceImpl(buyOrderRepository, new AuthenticationFacadeImpl(),
                employeeService, lendingOrderToolRepository, destructionOrderToolRepository, new BuyOrderMapperImpl(), toolService, toolRepository);
    }

    private BuyOrder getBuyOrder1(){
        BuyOrder buyOrder = new BuyOrder();
        buyOrder.setAddTimestamp(Timestamp.valueOf("2018-10-10 12:22:23"));
        buyOrder.setOrderCode("orderCode");
        buyOrder.addBuyOrderTool(getBuyOrderTools());
        buyOrder.setId(1L);

        return buyOrder;
    }

    private BuyOrder getBuyOrder2(){
        BuyOrder buyOrder = new BuyOrder();
        buyOrder.setAddTimestamp(Timestamp.valueOf("2018-10-10 12:22:23"));
        buyOrder.setOrderCode("123");
        buyOrder.addBuyOrderTool(getBuyOrderTools());
        buyOrder.setId(1L);

        return buyOrder;
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
        Tool tool = buyOrderTool.getTool();

        buyOrderService.checkIfUniqueToolWillNotBeOverloaded(tool, buyOrderTool.getCount());
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
        BuyOrder buyOrder = getBuyOrder1();
        Set<BuyOrderTool> buyOrderTools = buyOrder.getBuyOrderTools();
        Tool tool = buyOrderTools.iterator().next().getTool();

        buyOrderService.decreaseToolCountIfWasInPreviousBuyOrder(buyOrderTools, tool);

        assertEquals(997, tool.getAllCount().longValue());
        assertEquals(997, tool.getCurrentCount().longValue());
    }

    /*@Test
    public void changeBuyOrderTest() {
        BuyOrder buyOrder = getBuyOrder1();
        List<BuyOrderTool> buyOrderTools = buyOrder.getBuyOrderTools();
        Tool tool = buyOrderTools.get(0).getTool();

        BuyOrder newBuyOrder = buyOrder;
        newBuyOrder.getBuyOrderTools().forEach(buyOrderTool -> buyOrderTool.setCount(103L));

        buyOrderService.changeBuyOrder(buyOrder, newBuyOrder);

    }*/
}