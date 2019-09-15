package com.manageyourtools.toolroom.catalogue.web;

import com.manageyourtools.toolroom.catalogue.domain.CatalogueFacade;
import com.manageyourtools.toolroom.catalogue.dto.CategoryDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/categories")
@RestController
@AllArgsConstructor
public class CategoryController {

    private final CatalogueFacade catalogueFacade;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategory(@PathVariable Long id){
        return catalogueFacade.findCategory(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCategories(){
        return catalogueFacade.findAllCategories();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody CategoryDto categoryDto){
        return catalogueFacade.saveCategory(categoryDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable Long id){
        if (!categoryDto.getId().equals(id)) {
            throw new IllegalArgumentException("Category ID " + categoryDto.getId() + " does not match id " + id + " from path");
        }
        return catalogueFacade.saveCategory(categoryDto);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable Long id){
        catalogueFacade.deleteCategory(id);
    }

}
