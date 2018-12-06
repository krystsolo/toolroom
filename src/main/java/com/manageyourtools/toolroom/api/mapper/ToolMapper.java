package com.manageyourtools.toolroom.api.mapper;

import com.manageyourtools.toolroom.api.model.ToolDTO;
import com.manageyourtools.toolroom.domains.Tool;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE)
public interface ToolMapper {

    ToolMapper INSTANCE = Mappers.getMapper(ToolMapper.class);

    ToolDTO toolToToolDTO(Tool tool);
    Tool toolDtoToTool(ToolDTO toolDTO);
}
