package com.foodcourt.plaza_service.infrastructure.input.rest;

import com.foodcourt.plaza_service.application.dto.request.DishEnableDisableRequestDto;
import com.foodcourt.plaza_service.application.dto.request.DishRequestDto;
import com.foodcourt.plaza_service.application.dto.request.DishUpdateRequestDto;
import com.foodcourt.plaza_service.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishRestController {

    private final IDishHandler dishHandler;

    @Operation(summary = "Crear un nuevo plato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plato creado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado, rol incorrecto o no es propietario del restaurante", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurante o categoría no encontrados", content = @Content)
    })
    @PostMapping("/")
    @PreAuthorize("hasAuthority('ROLE_Propietario')")
    public ResponseEntity<Void> saveDish(@RequestBody DishRequestDto dishRequestDto){
        dishHandler.saveDish(dishRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Actualizar un plato existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado, no es propietario del restaurante", content = @Content),
            @ApiResponse(responseCode = "404", description = "Plato no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_Propietario')")
    public ResponseEntity<Void> updateDish(@PathVariable Long id, @RequestBody DishUpdateRequestDto dishUpdateRequestDto) {
        dishHandler.updateDish(id, dishUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Habilitar o deshabilitar un plato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del plato actualizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado, no es propietario del restaurante", content = @Content),
            @ApiResponse(responseCode = "404", description = "Plato no encontrado", content = @Content)
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_Propietario')")
    public ResponseEntity<Void> enableDisableDish(@PathVariable Long id, @RequestBody DishEnableDisableRequestDto dishEnableDisableRequestDto) {
        dishHandler.enableDisableDish(id, dishEnableDisableRequestDto);
        return ResponseEntity.ok().build();
    }
}
