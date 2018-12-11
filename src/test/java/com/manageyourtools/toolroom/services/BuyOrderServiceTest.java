package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.BuyOrderMapperImpl;
import com.manageyourtools.toolroom.api.model.BuyOrderDTO;
import com.manageyourtools.toolroom.config.AuthenticationFacadeImpl;
import com.manageyourtools.toolroom.domains.*;
import com.manageyourtools.toolroom.repositories.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class BuyOrderServiceTest {

    @Mock
    BuyOrderRepository buyOrderRepository;

    BuyOrderServiceImpl buyOrderService;
    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    LendingOrderToolRepository lendingOrderToolRepository;
    @Mock
    DestructionOrderToolRepository destructionOrderToolRepository;
    @Mock
    ToolRepository toolRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        buyOrderService = new BuyOrderServiceImpl(buyOrderRepository, new AuthenticationFacadeImpl(),
                employeeRepository, lendingOrderToolRepository, destructionOrderToolRepository, new BuyOrderMapperImpl(), toolRepository);
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

    @Test
    public void findAllBuyOrders() {
        Page<BuyOrder> buyOrders = new PageImpl<>(Arrays.asList(getBuyOrder1(), getBuyOrder2()),
                PageRequest.of(0,2),2);

        when(buyOrderRepository.findAll(any(Pageable.class))).thenReturn(buyOrders);

        Page<BuyOrderDTO> orders = buyOrderService.findAllBuyOrders(Pageable.unpaged());

        assertEquals(2, orders.getSize());
    }

    @Test
    public void findBuyOrderById() {
        BuyOrder buyOrder = getBuyOrder1();

        when(buyOrderRepository.findById(anyLong())).thenReturn(Optional.of(buyOrder));

        BuyOrderDTO order = buyOrderService.findBuyOrderById(2L);

        assertEquals(buyOrder.getOrderCode(), order.getOrderCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void uniqueToolHasAlreadyOneCopyOnWarehouse(){
        BuyOrderTool buyOrderTool = getBuyOrderTools();
        Tool tool = buyOrderTool.getTool();

        buyOrderService.checkIfUniqueToolWillNotBeOverloaded(tool, buyOrderTool.getCount());
    }

    @Test
    public void correctReturnToolCounts(){
        Tool tool = getBuyOrderTools().getTool();

        Tool retrievedTool = buyOrderService.backToToolCountBeforePurchase(1000L, tool);

        assertEquals(0, retrievedTool.getAllCount().longValue());
        assertEquals(0, retrievedTool.getCurrentCount().longValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void thereIsLendingOrDestructionOperationOnToolAfterBuyOrderSoThrowException(){

    }

    @Test
    public void thereIsLendingOperationOnToolAfterBuyOrder(){

    }

    @Test
    public void thereIsDestructionOperationOnToolAfterBuyOrder(){

    }

    @Test
    public void correctResultOfAdditionToolCount(){
        Tool tool = getBuyOrderTools().getTool();

        Tool retrievedTool = buyOrderService.additionToolCount(5L, tool);

        assertEquals(1005, retrievedTool.getAllCount().longValue());
        assertEquals(1005, retrievedTool.getCurrentCount().longValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void toolIsDisable(){
        BuyOrderTool buyOrderTool = getBuyOrderTools();
        Tool tool = buyOrderTool.getTool();

        buyOrderService.checkIfToolIsEnable(tool);
    }
}