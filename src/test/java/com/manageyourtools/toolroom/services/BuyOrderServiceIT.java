package com.manageyourtools.toolroom.services;

import com.manageyourtools.toolroom.api.mapper.BuyOrderMapperImpl;
import com.manageyourtools.toolroom.bootstrap.ToolRoomBootstrap;
import com.manageyourtools.toolroom.config.AuthenticationFacadeImpl;
import com.manageyourtools.toolroom.domains.*;
import com.manageyourtools.toolroom.repositories.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BuyOrderServiceIT {

    @Autowired
    BuyOrderRepository buyOrderRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    LendingOrderToolRepository lendingOrderToolRepository;
    @Autowired
    DestructionOrderToolRepository destructionOrderToolRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ToolRepository toolRepository;
    @Autowired
    BuyOrderToolRepository buyOrderToolRepository;

    @Autowired
            EmployeeService employeeService;
    @Autowired
            ToolService toolService;

    BuyOrderServiceImpl buyOrderService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ToolRoomBootstrap toolRoomBootstrap = new ToolRoomBootstrap(employeeRepository, roleRepository, categoryRepository,
                toolRepository, new BCryptPasswordEncoder(), buyOrderRepository, buyOrderToolRepository);

        toolRoomBootstrap.run();
        buyOrderService = new BuyOrderServiceImpl(buyOrderRepository, new AuthenticationFacadeImpl(),
                employeeService, lendingOrderToolRepository, destructionOrderToolRepository, new BuyOrderMapperImpl(), toolService);
    }

    @Test
    @Transactional
    public void deleteBuyOrder() {
        BuyOrder buyOrder = buyOrderRepository.getOne(1L);
        BuyOrderTool buyOrderTool = buyOrder.getBuyOrderTools().iterator().next();
        Tool tool = buyOrderTool.getTool();

        buyOrderService.deleteBuyOrder(1L);

        Optional<BuyOrder> deletedBuyOrder = buyOrderRepository.findById(1L);
        Optional<BuyOrderTool> buyOrderToolDeleted = buyOrderToolRepository.findById(buyOrderTool.getId());
        Optional<Tool> toolOptional = toolRepository.findById(tool.getId());

        assertFalse(deletedBuyOrder.isPresent());
        assertFalse(buyOrderToolDeleted.isPresent());
        assertTrue(toolOptional.isPresent());

        Tool updatedTool = toolOptional.get();
        Long expectedAllCount = tool.getAllCount() - buyOrderTool.getCount();
        Long expectedCurrentCount = tool.getCurrentCount() - buyOrderTool.getCount();

        assertEquals(expectedAllCount, updatedTool.getAllCount());
        assertEquals(expectedCurrentCount, updatedTool.getCurrentCount());
    }

    @Test
    public void addBuyOrder() {
    }

    @Test
    public void updateBuyOrder() {
    }

    @Test
    public void patchBuyOrder() {
    }


}
