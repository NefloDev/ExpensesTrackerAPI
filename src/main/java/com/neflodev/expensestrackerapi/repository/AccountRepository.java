package com.neflodev.expensestrackerapi.repository;

import com.neflodev.expensestrackerapi.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUser_Id(Long id);

}