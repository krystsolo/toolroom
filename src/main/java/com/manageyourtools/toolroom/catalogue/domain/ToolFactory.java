package com.manageyourtools.toolroom.catalogue.domain;

import com.manageyourtools.toolroom.catalogue.dto.ToolDto;
import com.manageyourtools.toolroom.catalogue.dto.ToolQueryDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
class ToolFactory {

    private final ToolRepository toolRepository;
    private final CategoryFactory categoryFactory;

    Tool from(ToolDto toolDto) {
        Tool.ToolBuilder toolBuilder = Tool.builder()
                .id(toolDto.getId())
                .category(categoryFactory.from(toolDto.getCategory()))
                .warrantyDate(toolDto.getWarrantyDate())
                .unitOfMeasure(toolDto.getUnitOfMeasure())
                .location(toolDto.getLocation())
                .name(toolDto.getName())
                .isToReturn(toolDto.getIsToReturn())
                .minimalCount(toolDto.getMinimalCount())
                .isUnique(toolDto.getIsUnique());
        if (toolDto.getId() != null) {
            ToolQueryDto tool = toolRepository.getToolOrThrow(toolDto.getId()).toQuery();
            if (uniqueChangeNotAllowed(tool, toolDto.getIsUnique())) {
                throw new IllegalArgumentException("Could not change value of unique field");
            }
            toolBuilder.isEnable(tool.getIsEnable())
                    .allCount(tool.getAllCount())
                    .currentCount(tool.getCurrentCount());
        } else {
            toolBuilder.isEnable(true)
                    .allCount(Tool.INITIAL_QUANTITY)
                    .currentCount(Tool.INITIAL_QUANTITY);
        }

        return toolBuilder.build();
    }

    private boolean uniqueChangeNotAllowed(ToolQueryDto tool, Boolean isUnique) {
        return isUnique && tool.getAllCount() > 0;
    }
}
