package com.neflodev.expensestrackerapi.web;

import com.neflodev.expensestrackerapi.dto.category.CategoryParams;
import com.neflodev.expensestrackerapi.dto.general.IdBody;
import com.neflodev.expensestrackerapi.service.CategoryService;
import com.neflodev.expensestrackerapi.util.CustomUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories/")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ResponseEntity<List<String>> getRetrieveAvailableCategories(){
        String sessionUsername = CustomUtils.retrieveSessionUsername();

        return ResponseEntity.ok(service.retrieveAvailableCategories(sessionUsername));
    }

    @PostMapping("/")
    public ResponseEntity<IdBody> postCreateCategory(@RequestBody CategoryParams params){
        String sessionUsername = CustomUtils.retrieveSessionUsername();

        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCategory(params, sessionUsername));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") Long categoryId){
        service.deleteCategory(categoryId);

        return ResponseEntity.ok().build();
    }

}
