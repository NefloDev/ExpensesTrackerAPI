package com.neflodev.expensestrackerapi.web;

import com.neflodev.expensestrackerapi.dto.general.IdBody;
import com.neflodev.expensestrackerapi.dto.movement.MovementDto;
import com.neflodev.expensestrackerapi.dto.movement.MovementParams;
import com.neflodev.expensestrackerapi.dto.movement.MovementRequestBody;
import com.neflodev.expensestrackerapi.service.MovementService;
import com.neflodev.expensestrackerapi.util.CustomUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("movements/")
public class MovementController {

    private final MovementService service;

    public MovementController(MovementService service) {
        this.service = service;
    }

    @PostMapping("/me")
    public ResponseEntity<List<MovementDto>> postRetrieveUserMovements(@RequestBody MovementRequestBody filters){
        String sessionUsername = CustomUtils.retrieveSessionUsername();

        return ResponseEntity.ok(service.retrieveUserMovements(filters, sessionUsername));
    }

    @GetMapping("/movementTypes")
    public ResponseEntity<List<String>> postRetrieveMovementTypes(){
        String sessionUsername = CustomUtils.retrieveSessionUsername();
        if (sessionUsername == null) {
            throw new UsernameNotFoundException("La sesión de usuario no es correcta");
        }

        return ResponseEntity.ok(service.retrieveMovementTypes());
    }

    @PostMapping("/")
    public ResponseEntity<IdBody> postCreateMovement(@RequestBody MovementParams params){
        String sessionUsername = CustomUtils.retrieveSessionUsername();

        return ResponseEntity.status(HttpStatus.CREATED).body(service.createMovement(params, sessionUsername));
    }

    @PutMapping("/")
    public ResponseEntity<IdBody> putUpdateMovement(@RequestBody MovementParams params){
        String sessionUsername = CustomUtils.retrieveSessionUsername();

        return ResponseEntity.ok(service.updateMovement(params, sessionUsername));
    }

    @DeleteMapping("/{movementId}")
    public ResponseEntity<Void> deleteMovement(@PathVariable("movementId") Long movementId){
        String sessionUsername = CustomUtils.retrieveSessionUsername();
        if (sessionUsername == null) {
            throw new UsernameNotFoundException("La sesión de usuario no es correcta");
        }
        service.deleteMovement(movementId);

        return ResponseEntity.ok().build();
    }

}
