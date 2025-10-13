package com.neflodev.expensestrackerapi.repository;

import com.neflodev.expensestrackerapi.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.username = ?1")
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsById(Long id);

}
