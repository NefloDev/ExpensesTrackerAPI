package com.neflodev.expensestrackerapi.repository;

import com.neflodev.expensestrackerapi.model.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovementEntityRepository extends JpaRepository<MovementEntity, Long> {

    @Query("""
            select m from MovementEntity m
            where m.account.accountName = ?1 and m.account.user.username = ?2 and (?3 is null or m.effectiveDate >= ?3) and (?4 is null or m.effectiveDate <= ?4)
            order by m.effectiveDate""")
    List<MovementEntity> findAllByAccountNameUserNameBetweenDates(String accountName, String username, @Nullable LocalDate effectiveDateStart, @Nullable LocalDate effectiveDateEnd);

}