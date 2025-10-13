package com.neflodev.expensestrackerapi.repository;

import com.neflodev.expensestrackerapi.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountEntityRepository extends JpaRepository<AccountEntity, Long> {

    List<AccountEntity> findByUser_Id(Long id);

    @Query("select a from AccountEntity a where a.user.username = ?1 and a.accountName = ?2")
    Optional<AccountEntity> findByUser_UsernameAndAccountName(String username, String accountName);


}