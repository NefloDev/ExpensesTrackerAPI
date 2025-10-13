package com.neflodev.expensestrackerapi.repository;

import com.neflodev.expensestrackerapi.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryEntityRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("select c from CategoryEntity c where c.user.username = ?1")
    List<CategoryEntity> findByUser_Username(String username);

    @Query("select (count(c) > 0) from CategoryEntity c where upper(c.categoryName) = upper(?1) and c.user.username = ?2")
    boolean existsByCategoryNameIgnoreCaseAndUser_Username(String categoryName, String username);

    @Query("select c from CategoryEntity c where c.user.username = ?1 and c.categoryName = ?2")
    Optional<CategoryEntity> findByUser_UsernameAndCategoryName(String username, String categoryName);


}