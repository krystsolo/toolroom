package com.manageyourtools.toolroom.api.mapper;

import com.manageyourtools.toolroom.api.model.LendingOrderToolDTO;
import com.manageyourtools.toolroom.domains.LendingOrderTool;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ToolMapper.class}, componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE)
public interface LendingOrderToolMapper {

    LendingOrderToolMapper INSTANCE = Mappers.getMapper(LendingOrderToolMapper.class);

    LendingOrderToolDTO lendingOrderToolToLendingOrderToolDTO(LendingOrderTool lendingOrderTool);

    LendingOrderTool lendingOrderToolDTOToLendingOrderTool(LendingOrderToolDTO lendingOrderToolDTO);
}
