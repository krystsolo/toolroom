package com.manageyourtools.toolroom.catalogue.domain;

import com.manageyourtools.toolroom.catalogue.dto.CategoryDto;

class CategoryFactory {

    Category from(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();

    }
}
