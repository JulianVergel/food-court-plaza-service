package com.foodcourt.plaza_service.infrastructure.input.rest;

import com.foodcourt.plaza_service.application.dto.request.DishRequestDto;
import com.foodcourt.plaza_service.application.handler.IDishHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishRestController {

    private final IDishHandler dishHandler;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('ROLE_Propietario')")
    public ResponseEntity<Void> saveDish(@RequestBody DishRequestDto dishRequestDto){
        dishHandler.saveDish(dishRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
