package com.manageyourtools.toolroom.bootstrap;

import com.manageyourtools.toolroom.domains.*;
import com.manageyourtools.toolroom.repositories.CategoryRepository;
import com.manageyourtools.toolroom.repositories.EmployeeRepository;
import com.manageyourtools.toolroom.repositories.RoleRepository;
import com.manageyourtools.toolroom.repositories.ToolRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class ToolRoomBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final ToolRepository toolRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ToolRoomBootstrap(EmployeeRepository employeeRepository, RoleRepository roleRepository, CategoryRepository categoryRepository, ToolRepository toolRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
        this.toolRepository = toolRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        employeeRepository.saveAll(getUsers());
        categoryRepository.saveAll(getCategories());
        toolRepository.saveAll(getTools());
    }

    private List<Employee> getUsers() {

        List<Employee> employees = new ArrayList<>(2);

        Role admin = new Role();
        admin.setRoleType(RoleEnum.ADMIN);
        Role worker = new Role();
        worker.setRoleType(RoleEnum.EMPLOYEE);
        Role warehouseman = new Role();
        warehouseman.setRoleType(RoleEnum.WAREHOUSEMAN);

        roleRepository.save(admin);
        roleRepository.save(worker);
        roleRepository.save(warehouseman);

        Employee employee1 = new Employee();
        employee1.setFirstName("Jan");
        employee1.setPassword(bCryptPasswordEncoder.encode("123"));
        employee1.setUserName("pko");
        employee1.setSurName("kowalski");
        employee1.setPhoneNumber(Long.valueOf("999999999"));
        employee1.setIsActive(false);

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(admin);

        employee1.setRoles(roleSet);
        employees.add(employee1);

        Employee employee2 = new Employee();
        employee2.setFirstName("Janek");
        employee2.setPassword(bCryptPasswordEncoder.encode("LOL"));
        employee2.setUserName("pawel");
        employee2.setSurName("kowalski2");
        employee2.setPhoneNumber(Long.valueOf("999999999"));
        employee2.setIsActive(true);

        Set<Role> roleSet2 = new HashSet<>();
        roleSet2.add(admin);
        roleSet2.add(worker);
        roleSet2.add(warehouseman);

        employee2.setRoles(roleSet2);
        employees.add(employee2);

        return employees;
    }

    private List<Category> getCategories() {
        Category other = new Category();
        other.setName("Other");
        Category mechanical = new Category();
        mechanical.setName("Mechanical");
        Category electrical = new Category();
        electrical.setName("Electrical");

        return Arrays.asList(other, electrical, electrical);
    }

    private List<Tool> getTools(){
        Tool hammer = new Tool();
        hammer.setAllCount(1L);
        hammer.setCurrentCount(0L);
        hammer.setIsToReturn(true);
        hammer.setIsUnique(true);
        hammer.setName("hammer");
        hammer.setUnitOfMeasure(UnitOfMeasure.PCS);

        Tool screwM6 = new Tool();
        screwM6.setAllCount(1000L);
        screwM6.setCurrentCount(1000L);
        screwM6.setIsToReturn(false);
        screwM6.setIsUnique(false);
        screwM6.setName("screw M6x30");
        screwM6.setUnitOfMeasure(UnitOfMeasure.PCS);

        return Arrays.asList(hammer, screwM6);
    }
}
