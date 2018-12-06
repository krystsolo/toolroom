package com.manageyourtools.toolroom.api.mapper;

import com.manageyourtools.toolroom.api.model.EmployeeDTO;

import com.manageyourtools.toolroom.domains.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {RoleMapper.class}, componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    Employee employeeDtoToEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO employeeToEmployeeDTO(Employee employee);
}
