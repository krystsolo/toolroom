package com.manageyourtools.toolroom.api.mapper;

import com.manageyourtools.toolroom.api.model.LendingOrderDTO;
import com.manageyourtools.toolroom.domains.LendingOrder;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {EmployeeMapper.class, LendingOrderToolMapper.class}, componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE)
public interface LendingOrderMapper {

    LendingOrderMapper INSTANCE = Mappers.getMapper(LendingOrderMapper.class);

    @InheritInverseConfiguration
    LendingOrder lendingOrderDTOToLendingOrder(LendingOrderDTO lendingOrderDTO);

    @Mapping(target = "warehousemanId", source = "lendingOrder.warehouseman.id")
    @Mapping(target = "workerId", source = "lendingOrder.worker.id")
    @Mapping(target = "lendingReturnOrderId", source = "lendingOrder.lendingReturnOrder.id")
    LendingOrderDTO lendingOrderToLendingOrderDTO(LendingOrder lendingOrder);
}
