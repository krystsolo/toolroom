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

    BuyOrderToolMapper INSTANCE = Mappers.getMapper(BuyOrderToolMapper.class);

    BuyOrderToolDTO buyOrderToolToBuyOrderToolDTO(BuyOrderTool buyOrderTool);

    BuyOrderTool buyOrderToolDTOToBuyOrderTool(BuyOrderToolDTO buyOrderToolDTO);
}
