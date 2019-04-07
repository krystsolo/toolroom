package com.manageyourtools.toolroom.api.mapper;

import com.manageyourtools.toolroom.api.model.DestructionOrderDTO;
import com.manageyourtools.toolroom.domains.DestructionOrder;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {EmployeeMapper.class, DestructionOrderToolMapper.class}, componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE)
public interface DestructionOrderMapper {

    DestructionOrderMapper INSTANCE = Mappers.getMapper(DestructionOrderMapper.class);

    @InheritInverseConfiguration
    DestructionOrder destructionOrderDtoToDestructionOrder(DestructionOrderDTO destructionOrderDTO);

    @Mapping(target = "warehousemanId", source = "destructionOrder.warehouseman.id")
    DestructionOrderDTO destructionOrderToDestructionOrderDto(DestructionOrder destructionOrder);
}
