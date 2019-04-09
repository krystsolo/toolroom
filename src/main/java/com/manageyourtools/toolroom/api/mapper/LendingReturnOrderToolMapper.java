package com.manageyourtools.toolroom.api.mapper;

import com.manageyourtools.toolroom.api.model.LendingReturnOrderToolDTO;
import com.manageyourtools.toolroom.domains.LendingReturnOrderTool;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ToolMapper.class}, componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE)
public interface LendingReturnOrderToolMapper {

    LendingReturnOrderToolMapper INSTANCE = Mappers.getMapper(LendingReturnOrderToolMapper.class);

    @Mapping(target = "returnWarehousemanId", source = "lendingReturnOrderTool.returnWarehouseman.id")
    LendingReturnOrderToolDTO lendingReturnOrderToolToLendingReturnOrderToolDTO(LendingReturnOrderTool lendingReturnOrderTool);

    @InheritInverseConfiguration
    LendingReturnOrderTool lendingReturnOrderToolDTOToLendingReturnOrderTool(LendingReturnOrderToolDTO lendingReturnOrderToolDTO);
}
