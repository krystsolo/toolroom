package com.manageyourtools.toolroom.api.mapper;

import com.manageyourtools.toolroom.api.model.RoleDTO;
import com.manageyourtools.toolroom.domains.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDTO roleToRoleDTO(Role role);
    Role roleDtoToRole(RoleDTO roleDTO);
}
