package com.neflodev.expensestrackerapi.service;

import com.neflodev.expensestrackerapi.constants.enums.MovementType;
import com.neflodev.expensestrackerapi.dto.general.IdBody;
import com.neflodev.expensestrackerapi.dto.movement.MovementDto;
import com.neflodev.expensestrackerapi.dto.movement.MovementFilters;
import com.neflodev.expensestrackerapi.dto.movement.MovementParams;
import com.neflodev.expensestrackerapi.dto.movement.MovementRequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MovementServiceTest {

    private static final LocalDate LOCAL_DATE = LocalDate.of(2025, 1, 1);
    private static final String USER_NAME = "User";
    private static final String ACCOUNT_NAME = "Test Account";

    @MockitoBean
    Clock clock;

    Clock fixedClock;

    @Autowired
    MovementService service;

    @BeforeEach
    void initMocks(){
        MockitoAnnotations.openMocks(this);

        fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }

    @Test
    void retrieveCurrentMonthUserMovementsTest() {
        List<MovementDto> movements = service.retrieveUserMovements(new MovementRequestBody(ACCOUNT_NAME, null, null), USER_NAME);

        assertNotNull(movements);
        assertFalse(movements.isEmpty());
    }

    @Test
    void retrieveSpecificDatesUserMovementsTest() {
        LocalDate fromDate = LocalDate.of(2025, 1, 1);
        LocalDate toDate = LocalDate.of(2025, 12, 31);
        List<MovementDto> movements = service.retrieveUserMovements(new MovementRequestBody(ACCOUNT_NAME, fromDate, toDate), USER_NAME);

        assertNotNull(movements);
        assertFalse(movements.isEmpty());
    }

    @Test
    void retrieveNotFoundUserMovementsTest() {
        LocalDate fromDate = LocalDate.of(2025, 3, 1);
        LocalDate toDate = LocalDate.of(2025, 5, 31);
        List<MovementDto> movements = service.retrieveUserMovements(new MovementRequestBody(ACCOUNT_NAME, fromDate, toDate), USER_NAME);

        assertNotNull(movements);
        assertTrue(movements.isEmpty());
    }

    @Test
    void retrieveMovementTypesTest() {
        MovementFilters response = service.retrieveMovementFilters(USER_NAME);
        List<String> expectedMovementTypes = Arrays.stream(MovementType.values()).map(MovementType::name).toList();

        assertNotNull(response);
        assertEquals(expectedMovementTypes, response.movementTypes());
        assertNotNull(response.categories());
        assertFalse(response.categories().isEmpty());
    }

    @Test
    void createMovementsTest() {
        MovementParams incomeMovement = new MovementParams();
        incomeMovement.setMovementType(MovementType.INCOME);
        incomeMovement.setAmount(BigDecimal.valueOf(1234.56));
        incomeMovement.setAccountName(ACCOUNT_NAME);
        incomeMovement.setCategory("Income");

        IdBody incomeResponse = service.createMovement(incomeMovement, USER_NAME);

        assertNotNull(incomeResponse);
        MovementParams expenseMovement = new MovementParams();
        expenseMovement.setMovementType(MovementType.INCOME);
        expenseMovement.setAmount(BigDecimal.valueOf(12.34));
        expenseMovement.setAccountName(ACCOUNT_NAME);
        expenseMovement.setCategory("Utilities");
        expenseMovement.setComment("Kitchen utensils");

        IdBody expenseResponse = service.createMovement(expenseMovement, USER_NAME);

        assertNotNull(expenseResponse);
    }

    @Test
    void updateMovementTest() {
        MovementParams updateMovement = new MovementParams();
        updateMovement.setId(996L);
        updateMovement.setMovementType(MovementType.EXPENSE);
        updateMovement.setAmount(BigDecimal.valueOf(222.33));
        updateMovement.setAccountName(ACCOUNT_NAME);

        IdBody incomeResponse = service.updateMovement(updateMovement, USER_NAME);

        assertNotNull(incomeResponse);
    }

    @Test
    void deleteMovementTest() {
        LocalDate fromDate = LocalDate.of(2025, 1, 1);
        LocalDate toDate = LocalDate.of(2025, 12, 31);

        int count = service.retrieveUserMovements(new MovementRequestBody(ACCOUNT_NAME, fromDate, toDate), USER_NAME).size();

        service.deleteMovement(994L);

        int count2 = service.retrieveUserMovements(new MovementRequestBody(ACCOUNT_NAME, fromDate, toDate), USER_NAME).size();

        assertTrue(count > count2);
    }
}