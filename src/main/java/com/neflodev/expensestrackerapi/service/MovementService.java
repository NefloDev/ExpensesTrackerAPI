package com.neflodev.expensestrackerapi.service;

import com.neflodev.expensestrackerapi.constants.ExceptionsConst;
import com.neflodev.expensestrackerapi.constants.enums.MovementType;
import com.neflodev.expensestrackerapi.dto.general.IdBody;
import com.neflodev.expensestrackerapi.dto.movement.MovementDto;
import com.neflodev.expensestrackerapi.dto.movement.MovementParams;
import com.neflodev.expensestrackerapi.dto.movement.MovementRequestBody;
import com.neflodev.expensestrackerapi.mapper.MovementMapper;
import com.neflodev.expensestrackerapi.model.AccountEntity;
import com.neflodev.expensestrackerapi.model.CategoryEntity;
import com.neflodev.expensestrackerapi.model.MovementEntity;
import com.neflodev.expensestrackerapi.model.UserEntity;
import com.neflodev.expensestrackerapi.repository.AccountEntityRepository;
import com.neflodev.expensestrackerapi.repository.CategoryEntityRepository;
import com.neflodev.expensestrackerapi.repository.MovementEntityRepository;
import com.neflodev.expensestrackerapi.repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.neflodev.expensestrackerapi.constants.CustomConstants.MINUS_ONE;

@Slf4j
@Service
public class MovementService {

    private final MovementEntityRepository repo;
    private final MovementMapper mapper;
    private final CategoryEntityRepository categoryRepo;
    private final AccountEntityRepository accountRepo;
    private final UserEntityRepository userRepo;

    @Autowired
    public MovementService(MovementEntityRepository repo, MovementMapper mapper, CategoryEntityRepository categoryRepo, AccountEntityRepository accountRepo, UserEntityRepository userRepo) {
        this.repo = repo;
        this.mapper = mapper;
        this.categoryRepo = categoryRepo;
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
    }

    public List<MovementDto> retrieveUserMovements(MovementRequestBody filters, String username){
        List<MovementEntity> userMovements = repo.findByAccount_AccountNameAndAccount_User_Username(filters.accountName(), username);

        if (userMovements.isEmpty()){
            log.info("com.neflodev.expensestrackerapi.service.MovementService.retrieveUserMovements() >> No movements found for user {}", username);
            return new ArrayList<>();
        }

        return userMovements.stream().map(mapper::entityToDto).toList();
    }

    public IdBody createMovement(MovementParams params, String username){
        MovementEntity movement = createMovementEntity(params, username);

        return new IdBody(repo.save(movement).getId());
    }

    public IdBody updateMovement(MovementParams params, String username){
        MovementEntity source = repo.findById(params.getId()).orElseThrow(() -> ExceptionsConst.MOVEMENT_NOT_FOUND_EXCEPTION);
        MovementEntity movement = createMovementEntity(params, username);

        AccountEntity account = movement.getAccount();
        account.setBalance(account.getBalance().subtract(source.getAmount()));

        mapper.updateMovement(source, movement);

        return new IdBody(repo.save(source).getId());
    }

    public void deleteMovement(Long movementId){
        if (!repo.existsById(movementId)) {
            throw ExceptionsConst.MOVEMENT_NOT_FOUND_EXCEPTION;
        }

        repo.deleteById(movementId);
    }

    private MovementEntity createMovementEntity(MovementParams params, String username) {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> ExceptionsConst.USER_NOT_FOUND_EXCEPTION);
        AccountEntity account = accountRepo.findByUser_UsernameAndAccountName(user.getUsername(), params.getAccountName())
                .orElseThrow(() -> ExceptionsConst.ACCOUNT_NOT_FOUND_EXCEPTION);
        CategoryEntity category = categoryRepo.findByUser_UsernameAndCategoryName(user.getUsername(), params.getCategory())
                .orElseThrow(() -> ExceptionsConst.CATEGORY_NOT_FOUND_EXCEPTION);

        AccountEntity destination = null;
        if (params.getMovementType() == MovementType.TRANSFER) {
            destination = accountRepo.findByUser_UsernameAndAccountName(user.getUsername(), params.getDestinationAccount())
                    .orElseThrow(() -> ExceptionsConst.DESTINATION_ACCOUNT_NOT_FOUND_EXCEPTION);
        }

        BigDecimal finalAmount = switch (params.getMovementType()) {
            case INCOME -> params.getAmount();
            case EXPENSE, TRANSFER -> params.getAmount().abs().multiply(MINUS_ONE);
        };

        account.setBalance(account.getBalance().add(finalAmount));
        accountRepo.save(account);

        MovementEntity movement = new MovementEntity();
        movement.setAccount(account);
        movement.setCategory(category);
        movement.setAmount(finalAmount);
        movement.setDestination(destination);
        movement.setComment(params.getComment());
        return movement;
    }

}
