package com.manageyourtools.toolroom.bootstrap;

import com.manageyourtools.toolroom.domains.*;
import com.manageyourtools.toolroom.exception.ResourceNotFoundException;
import com.manageyourtools.toolroom.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

@Component
public class ToolRoomBootstrap implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final ToolRepository toolRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final BuyOrderRepository buyOrderRepository;
    private final BuyOrderToolRepository buyOrderToolRepository;

    public ToolRoomBootstrap(EmployeeRepository employeeRepository, RoleRepository roleRepository, CategoryRepository categoryRepository, ToolRepository toolRepository, BCryptPasswordEncoder bCryptPasswordEncoder, BuyOrderRepository buyOrderRepository, BuyOrderToolRepository buyOrderToolRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.toolRepository = toolRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.buyOrderRepository = buyOrderRepository;
        this.buyOrderToolRepository = buyOrderToolRepository;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        roleRepository.saveAll(getRoles());
        employeeRepository.saveAll(getUsers());
        categoryRepository.saveAll(getCategories());
        buyOrderRepository.saveAll(Arrays.asList(getBuyOrder1(), getBuyOrder2()));
        toolRepository.saveAll(getTools());
    }
    private List<Role> getRoles(){
        Role admin = new Role();
        admin.setRoleType(RoleEnum.ADMIN);
        admin.setId(1L);
        Role worker = new Role();
        worker.setRoleType(RoleEnum.EMPLOYEE);
        worker.setId(2L);
        Role warehouseman = new Role();
        warehouseman.setRoleType(RoleEnum.WAREHOUSEMAN);
        warehouseman.setId(3L);
        return Arrays.asList(admin, worker, warehouseman);
    }

    private List<Employee> getUsers() {

        Role admin = roleRepository.findByRoleType(RoleEnum.ADMIN);
        Role worker = roleRepository.findByRoleType(RoleEnum.EMPLOYEE);
        Role warehouseman = roleRepository.findByRoleType(RoleEnum.WAREHOUSEMAN);

        Employee employee1 = new Employee();
        employee1.setFirstName("Jan");
        employee1.setPassword(bCryptPasswordEncoder.encode("123"));
        employee1.setUserName("pko");
        employee1.setSurName("kowalski");
        employee1.setPhoneNumber(Long.valueOf("999999999"));
        employee1.setIsActive(false);
        employee1.setId(1L);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(admin);
        roleSet.add(worker);
        employee1.setRoles(roleSet);


        Employee employee2 = new Employee();
        employee2.setFirstName("Janek");
        employee2.setPassword(bCryptPasswordEncoder.encode("LOL"));
        employee2.setUserName("pawel");
        employee2.setSurName("kowalski2");
        employee2.setPhoneNumber(Long.valueOf("999999999"));
        employee2.setIsActive(true);
        employee2.setId(2L);

        Set<Role> roleSet2 = new HashSet<>();
        roleSet2.add(admin);
        roleSet2.add(worker);
        roleSet2.add(warehouseman);

        employee2.setRoles(roleSet2);



        return Arrays.asList(employee1, employee2);
    }

    private List<Category> getCategories() {
        Category other = new Category();
        other.setName("Other");
        other.setId(1L);
        Category mechanical = new Category();
        mechanical.setName("Mechanical");
        mechanical.setId(2L);
        Category electrical = new Category();
        electrical.setName("Electrical");
        electrical.setId(3L);

        return Arrays.asList(other, electrical, mechanical);
    }

    private List<Tool> getTools(){
        Tool hammer = new Tool();
        hammer.setAllCount(1L);
        hammer.setCurrentCount(0L);
        hammer.setIsToReturn(true);
        hammer.setIsUnique(true);
        hammer.setName("hammer");
        hammer.setUnitOfMeasure(UnitOfMeasure.PCS);
        hammer = hammer.addBuyOrderTool(buyOrderToolRepository.getOne(1L));

        Tool screwM6 = new Tool();
        screwM6.setAllCount(1000L);
        screwM6.setCurrentCount(1000L);
        screwM6.setIsToReturn(false);
        screwM6.setIsUnique(false);
        screwM6.setName("screw M6x30");
        screwM6.setUnitOfMeasure(UnitOfMeasure.PCS);
        screwM6 = screwM6.addBuyOrderTool(buyOrderToolRepository.getOne(2L));

        return Arrays.asList(hammer, screwM6);
    }

    private BuyOrder getBuyOrder1(){
        BuyOrder buyOrder = new BuyOrder();
        buyOrder.setAddTimestamp(Timestamp.valueOf("2018-10-10 12:22:23"));

        buyOrder.setOrderCode("orderCode");

        buyOrder.setWarehouseman(employeeRepository.findByUserName("pko"));
        buyOrder.setId(1L);
        buyOrder.addBuyOrderTool(getBuyOrderTools().get(0));
        buyOrder.addBuyOrderTool(getBuyOrderTools().get(1));
        return buyOrder;
    }

    private BuyOrder getBuyOrder2(){
        BuyOrder buyOrder = new BuyOrder();
        buyOrder.setAddTimestamp(Timestamp.valueOf("2018-10-10 12:22:23"));
        buyOrder.setWarehouseman(employeeRepository.findByUserName("pko"));
        buyOrder.setOrderCode("123");
        buyOrder.setId(2L);


        return buyOrder;
    }

    private List<BuyOrderTool> getBuyOrderTools(){
        BuyOrderTool buyOrderTool1 = new BuyOrderTool();
        buyOrderTool1.setCount(1L);
        buyOrderTool1.setId(1L);

        BuyOrderTool buyOrderTool2 = new BuyOrderTool();
        buyOrderTool2.setId(2L);
        buyOrderTool2.setCount(3L);

        return Arrays.asList(buyOrderTool1, buyOrderTool2) ;
    }


}
