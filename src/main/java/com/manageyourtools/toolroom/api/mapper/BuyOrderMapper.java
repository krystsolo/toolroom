package com.manageyourtools.toolroom.api.mapper;

import com.manageyourtools.toolroom.api.model.BuyOrderDTO;
import com.manageyourtools.toolroom.api.model.BuyOrderToolDTO;
import com.manageyourtools.toolroom.api.model.EmployeeDTO;
import com.manageyourtools.toolroom.domains.BuyOrder;
import com.manageyourtools.toolroom.domains.BuyOrderTool;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {EmployeeMapper.class, BuyOrderToolMapper.class}, componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE)
public interface BuyOrderMapper {

    BuyOrderMapper INSTANCE = Mappers.getMapper(BuyOrderMapper.class);

    @InheritInverseConfiguration
    BuyOrder buyOrderDtoToBuyOrder(BuyOrderDTO buyOrderDTO);

    @Mapping(target = "warehousemanUsername", source = "buyOrder.warehouseman.userName")
    BuyOrderDTO buyOrderToBuyOrderDTO(BuyOrder buyOrder);

}
