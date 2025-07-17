package com.foodcourt.plaza_service.infrastructure.input.rest;

import com.foodcourt.plaza_service.application.dto.request.RestaurantRequestDto;
import com.foodcourt.plaza_service.domain.api.IRestaurantServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantRestController {
    private final IRestaurantServicePort restaurantServicePort;

    @Operation(summary = "Crear un nuevo restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante creado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Restaurante ya existe", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<Void> saveRestaurant(@RequestBody RestaurantRequestDto restaurantRequestDto) {
        restaurantServicePort.saveRestaurant(restaurantRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
