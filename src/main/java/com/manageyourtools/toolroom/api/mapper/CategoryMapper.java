package com.manageyourtools.toolroom.api.mapper;

import com.manageyourtools.toolroom.api.model.CategoryDTO;
import com.manageyourtools.toolroom.api.model.RoleDTO;
import com.manageyourtools.toolroom.domains.Category;
import com.manageyourtools.toolroom.domains.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", typeConversionPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO categoryToCategoryDTO(Category category);
    Category categoryDTOToCategory(CategoryDTO categoryDTO);

}
