package com.manageyourtools.toolroom.controllers;

import com.manageyourtools.toolroom.api.model.CategoryDTO;
import com.manageyourtools.toolroom.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/categories")
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO getCategory(@PathVariable Long id){

        return categoryService.findCategoryById(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDTO> getCategories(){

        return categoryService.findAllCategories();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO addCategory(@RequestBody CategoryDTO categoryDTO){

        return categoryService.addCategory(categoryDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Long id){

        return categoryService.updateCategory(id, categoryDTO);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable Long id){

        categoryService.deleteCategory(id);
    }

}
