package com.neflodev.expensestrackerapi.repository;

import com.neflodev.expensestrackerapi.model.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementEntityRepository extends JpaRepository<MovementEntity, Long> {

    @Query("select m from MovementEntity m where m.account.accountName = ?1 and m.account.user.username = ?2")
    List<MovementEntity> findByAccount_AccountNameAndAccount_User_Username(String accountName, String username);


}