package com.manageyourtools.toolroom.api.mapper;

import com.manageyourtools.toolroom.api.model.DestructionOrderToolDTO;
import com.manageyourtools.toolroom.domains.DestructionOrderTool;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ToolMapper.class}, componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE)
public interface DestructionOrderToolMapper {

    DestructionOrderToolMapper INSTANCE = Mappers.getMapper(DestructionOrderToolMapper.class);

    DestructionOrderToolDTO destructionOrderToolToDestructionOrderToolDTO(DestructionOrderTool destructionOrderTool);

    DestructionOrderTool destructionOrderToolDTOToDestructionOrderTool(DestructionOrderToolDTO destructionOrderToolDTO);
}
