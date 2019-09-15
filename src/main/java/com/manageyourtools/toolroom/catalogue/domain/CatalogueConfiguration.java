package com.manageyourtools.toolroom.catalogue.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
class CatalogueConfiguration {

    @Bean
    CatalogueFacade catalogueFacade(ToolRepository toolRepository, ToolImageRepository toolImageRepository, CategoryRepository categoryRepository) {
        CategoryFactory categoryFactory = new CategoryFactory();
        ToolFactory toolFactory = new ToolFactory(toolRepository, categoryFactory);
        return new CatalogueFacade(toolRepository, toolFactory, toolImageRepository, categoryFactory, categoryRepository);
    }

    CatalogueFacade catalogueFacade(Map<Long, Tool> toolDb, Map<Long, ToolImage> toolImageDb, Map<Long, Category> categoryDb) {
        CategoryFactory categoryFactory = new CategoryFactory();
        ToolRepository toolRepository = new InMemoryToolRepository(toolDb);
        ToolFactory toolFactory = new ToolFactory(toolRepository, categoryFactory);
        ToolImageRepository toolImageRepository = new InMemoryToolImageRepository(toolImageDb);
        CategoryRepository categoryRepository = new InMemoryCategoryRepository(categoryDb);
        return new CatalogueFacade(toolRepository, toolFactory, toolImageRepository, categoryFactory, categoryRepository);
    }
}
