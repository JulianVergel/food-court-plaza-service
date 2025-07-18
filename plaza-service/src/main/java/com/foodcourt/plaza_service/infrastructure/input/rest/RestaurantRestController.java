package com.foodcourt.plaza_service.infrastructure.input.rest;

import com.foodcourt.plaza_service.application.dto.request.RestaurantRequestDto;
import com.foodcourt.plaza_service.application.dto.response.RestaurantListResponseDto;
import com.foodcourt.plaza_service.application.handler.IRestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantRestController {
    private final IRestaurantHandler restaurantHandler;

    @Operation(summary = "Crear un nuevo restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante creado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado, el usuario especificado no es propietario", content = @Content),
            @ApiResponse(responseCode = "409", description = "Restaurante ya existe (ej: NIT duplicado)", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<Void> saveRestaurant(@RequestBody RestaurantRequestDto restaurantRequestDto) {
        restaurantHandler.saveRestaurant(restaurantRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Listar restaurantes paginados y ordenados alfabéticamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurantes listados correctamente", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autorizado para ver el recurso", content = @Content)
    })
    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<RestaurantListResponseDto>> listRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(restaurantHandler.listRestaurants(page, size));
    }
}
