package com.manageyourtools.toolroom.api.mapper;

import com.manageyourtools.toolroom.api.model.BuyOrderDTO;
import com.manageyourtools.toolroom.api.model.BuyOrderToolDTO;
import com.manageyourtools.toolroom.domains.BuyOrder;
import com.manageyourtools.toolroom.domains.BuyOrderTool;
import com.manageyourtools.toolroom.domains.Tool;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ToolMapper.class}, componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE)
public interface BuyOrderToolMapper {

    BuyOrderMapper INSTANCE = Mappers.getMapper(BuyOrderMapper.class);

    @Mapping(target = "toolId", source = "buyOrderTool.tool.id")
    BuyOrderToolDTO buyOrderToolToBuyOrderToolDTO(BuyOrderTool buyOrderTool);

    //@Mapping(source = "buyOrderToolDTO.toolId", target = "tool.id")
   // BuyOrderTool buyOrderToolDTOToBuyOrderTool(BuyOrderToolDTO buyOrderToolDTO);
}
