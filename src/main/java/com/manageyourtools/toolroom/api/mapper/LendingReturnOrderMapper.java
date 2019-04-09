package com.manageyourtools.toolroom.api.mapper;

import com.manageyourtools.toolroom.api.model.LendingReturnOrderDTO;
import com.manageyourtools.toolroom.domains.LendingReturnOrder;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {EmployeeMapper.class, LendingReturnOrderToolMapper.class}, componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE)
public interface LendingReturnOrderMapper {

    LendingReturnOrderMapper INSTANCE = Mappers.getMapper(LendingReturnOrderMapper.class);

    @InheritInverseConfiguration
    LendingReturnOrder lendingReturnOrderDTOToLendingReturnOrder(LendingReturnOrderDTO lendingReturnOrderDTO);

    @Mapping(target = "returnWarehousemanId", source = "lendingReturnOrder.returnWarehouseman.id")
    @Mapping(target = "workerId", source = "lendingReturnOrder.worker.id")
    @Mapping(target = "lendingOrderId", source = "lendingReturnOrder.lendingOrder.id")
    LendingReturnOrderDTO lendingReturnOrderToLendingReturnOrderDTO(LendingReturnOrder lendingReturnOrder);
}
