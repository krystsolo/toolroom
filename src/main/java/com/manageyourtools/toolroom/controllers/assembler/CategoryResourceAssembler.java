package com.manageyourtools.toolroom.controllers.assembler;

import com.manageyourtools.toolroom.api.model.CategoryDTO;
import com.manageyourtools.toolroom.controllers.CategoryController;
import com.manageyourtools.toolroom.domains.Category;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class CategoryResourceAssembler implements ResourceAssembler<CategoryDTO, Resource<CategoryDTO>> {
    @Override
    public Resource<CategoryDTO> toResource(CategoryDTO categoryDTO) {
        return new Resource<>(categoryDTO,
                ControllerLinkBuilder.linkTo(methodOn(CategoryController.class).getCategory(categoryDTO.getId())).withSelfRel(),
                linkTo(methodOn(CategoryController.class).getCategories(Sort.by("name").ascending())).withRel("categories"));
    }
}
