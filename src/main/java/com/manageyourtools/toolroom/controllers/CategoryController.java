package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.CategoryDTO;
import com.manageyourtools.toolroom.controllers.assembler.CategoryResourceAssembler;
import com.manageyourtools.toolroom.domains.Category;
import com.manageyourtools.toolroom.services.CategoryService;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RequestMapping("/categories")
@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryResourceAssembler assembler;

    public CategoryController(CategoryService categoryService, CategoryResourceAssembler assembler) {
        this.categoryService = categoryService;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Resource<CategoryDTO> getCategory(@PathVariable Long id){

        CategoryDTO categoryDTO = categoryService.findCategoryById(id);

        return assembler.toResource(categoryDTO);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Resources<Resource<CategoryDTO>> getCategories(Sort sort){

        List<Resource<CategoryDTO>> resource = categoryService.findAllCategories(sort).stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(resource,
                linkTo(methodOn(CategoryController.class).getCategories(sort)).withSelfRel());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO){

        return assembler.toResource(categoryService.addCategory(categoryDTO));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Resource<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Long id){

        return assembler.toResource(categoryService.updateCategory(id, categoryDTO));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable Long id){

        categoryService.deleteCategory(id);
    }

}
