package com.manageyourtools.toolroom.catalogue.domain

import groovy.transform.CompileStatic

import java.time.LocalDate

@CompileStatic
trait SampleTools {

    Tool hammer = createTool(1, "hammerDto", 5, false, true, null, true, 3, 3)
    Tool drill = createTool(10, "drill", null, true, true, LocalDate.now().plusDays(1), true, Tool.INITIAL_QUANTITY, Tool.INITIAL_QUANTITY)

    static private Tool createTool(Long id, String name, Long minimalCount, boolean isUnique, boolean isToReturn, LocalDate warrantyDate, Boolean isEnable, Long allCount, Long currentCount) {
            return Tool.builder()
                    .id(id)
                    .name(name)
                    .category(null)
                    .unitOfMeasure(UnitOfMeasure.PCS)
                    .minimalCount(minimalCount)
                    .isUnique(isUnique)
                    .isToReturn(isToReturn)
                    .warrantyDate(warrantyDate)
                    .location("location")
                    .isEnable(isEnable)
                    .allCount(allCount)
                    .currentCount(currentCount)
                    .build()
    }

}