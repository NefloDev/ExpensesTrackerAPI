package com.neflodev.expensestrackerapi.service;

import com.neflodev.expensestrackerapi.constants.ExceptionsConst;
import com.neflodev.expensestrackerapi.dto.category.CategoryParams;
import com.neflodev.expensestrackerapi.dto.general.IdBody;
import com.neflodev.expensestrackerapi.exception.custom.BadRequestException;
import com.neflodev.expensestrackerapi.model.CategoryEntity;
import com.neflodev.expensestrackerapi.model.UserEntity;
import com.neflodev.expensestrackerapi.repository.CategoryEntityRepository;
import com.neflodev.expensestrackerapi.repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CategoryService {

    private final CategoryEntityRepository categoryRepo;
    private final UserEntityRepository userRepo;

    @Autowired
    public CategoryService(CategoryEntityRepository categoryRepo, UserEntityRepository userRepo) {
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
    }

    public List<String> retrieveAvailableCategories(String username){
        if (!userRepo.existsByUsername(username)) {
            throw ExceptionsConst.USER_NOT_FOUND_EXCEPTION;
        }
        List<CategoryEntity> userCategories = categoryRepo.findByUser_Username(username);

        if (userCategories.isEmpty()){
            log.info("com.neflodev.expensestrackerapi.service.MovementService.retrieveUserMovements() >> No categories found for user {}", username);
            return new ArrayList<>();
        }
        return userCategories.stream().map(CategoryEntity::getCategoryName).toList();
    }

    public IdBody createCategory(CategoryParams params, String username) {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> ExceptionsConst.USER_NOT_FOUND_EXCEPTION);

        if (categoryRepo.existsByCategoryNameIgnoreCaseAndUser_Username(params.categoryName(), username)){
            throw new BadRequestException("A category with that name already exists");
        }

        CategoryEntity category = new CategoryEntity();
        category.setCategoryName(params.categoryName());
        category.setUser(user);

        return new IdBody(categoryRepo.save(category).getId());
    }

    public void deleteCategory(Long categoryId){
        if (!categoryRepo.existsById(categoryId)) {
            throw ExceptionsConst.CATEGORY_NOT_FOUND_EXCEPTION;
        }

        categoryRepo.deleteById(categoryId);
    }

}
