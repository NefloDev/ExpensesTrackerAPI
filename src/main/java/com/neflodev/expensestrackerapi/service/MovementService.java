package com.neflodev.expensestrackerapi.service;

import com.neflodev.expensestrackerapi.constants.ExceptionsConst;
import com.neflodev.expensestrackerapi.constants.enums.MovementType;
import com.neflodev.expensestrackerapi.dto.general.IdBody;
import com.neflodev.expensestrackerapi.dto.movement.MovementDto;
import com.neflodev.expensestrackerapi.dto.movement.MovementFilters;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.neflodev.expensestrackerapi.constants.CustomConstants.MINUS_ONE;

@Slf4j
@AllArgsConstructor
@Service
public class MovementService {

    private final MovementEntityRepository repo;
    private final MovementMapper mapper;
    private final CategoryEntityRepository categoryRepo;
    private final AccountEntityRepository accountRepo;
    private final UserEntityRepository userRepo;
    private final Clock clock;

    public List<MovementDto> retrieveUserMovements(MovementRequestBody filters, String username){
        LocalDate from;
        LocalDate to;

        if (filters.startDate() == null && filters.endDate() == null){
            YearMonth currentMonth = YearMonth.now(clock);
            from = currentMonth.atDay(1);
            to = currentMonth.atEndOfMonth();
        }else{
            from = filters.startDate();
            to = filters.endDate();
        }

        List<MovementEntity> userMovements = repo.findAllByAccountNameUserNameBetweenDates(filters.accountName(), username, from, to);

        if (userMovements.isEmpty()){
            log.info("com.neflodev.expensestrackerapi.service.MovementService.retrieveUserMovements() >> No movements found for user {}", username);
            return new ArrayList<>();
        }

        return userMovements.stream().map(mapper::entityToDto).toList();
    }

    public MovementFilters retrieveMovementFilters(String username) {
        List<String> movementTypes = Arrays.stream(MovementType.values()).map(MovementType::name).toList();
        List<String> categories = categoryRepo.findByUser_Username(username).stream().map(CategoryEntity::getCategoryName).toList();

        return new MovementFilters(movementTypes, categories);
    }

    public IdBody createMovement(MovementParams params, String username){
        MovementEntity movement = createMovementEntity(params, username);

        return new IdBody(repo.save(movement).getId());
    }

    public IdBody updateMovement(MovementParams params, String username){
        MovementEntity source = repo.findById(params.getId()).orElseThrow(() -> ExceptionsConst.MOVEMENT_NOT_FOUND_EXCEPTION);
        MovementEntity movement = createMovementEntity(params, username, true);

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
        return createMovementEntity(params, username, false);
    }

    private MovementEntity createMovementEntity(MovementParams params, String username, boolean isUpdate) {
        UserEntity user = userRepo.findByUsername(username)
                .orElseThrow(() -> ExceptionsConst.USER_NOT_FOUND_EXCEPTION);
        AccountEntity account = accountRepo.findByUser_UsernameAndAccountName(user.getUsername(), params.getAccountName())
                .orElseThrow(() -> ExceptionsConst.ACCOUNT_NOT_FOUND_EXCEPTION);
        CategoryEntity category = !isUpdate ? categoryRepo.findByUser_UsernameAndCategoryName(user.getUsername(), params.getCategory())
                .orElseThrow(() -> ExceptionsConst.CATEGORY_NOT_FOUND_EXCEPTION) : null;

        AccountEntity destination = null;
        if (params.getMovementType() == MovementType.TRANSFER) {
            destination = accountRepo.findByUser_UsernameAndAccountName(user.getUsername(), params.getDestinationAccount())
                    .orElseThrow(() -> ExceptionsConst.DESTINATION_ACCOUNT_NOT_FOUND_EXCEPTION);
        }

        BigDecimal finalAmount = switch (params.getMovementType()) {
            case INCOME -> params.getAmount();
            case EXPENSE, TRANSFER -> params.getAmount().abs().multiply(MINUS_ONE);
        };

        account.setBalance(Objects.requireNonNullElse(account.getBalance(), BigDecimal.ZERO).add(finalAmount));
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
