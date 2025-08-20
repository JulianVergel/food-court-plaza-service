package com.foodcourt.plaza_service.infrastructure.input.rest;

import com.foodcourt.plaza_service.application.dto.request.DishEnableDisableRequestDto;
import com.foodcourt.plaza_service.application.dto.request.DishRequestDto;
import com.foodcourt.plaza_service.application.dto.request.DishUpdateRequestDto;
import com.foodcourt.plaza_service.application.dto.response.DishListResponseDto;
import com.foodcourt.plaza_service.application.handler.IDishHandler;
import com.foodcourt.plaza_service.domain.model.PaginationResponse;
import com.foodcourt.plaza_service.infrastructure.input.doc.StandardApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.foodcourt.plaza_service.infrastructure.input.doc.SwaggerConstants.*;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishRestController {

    private final IDishHandler dishHandler;

    @Operation(summary = DISH_CREATE_SUMMARY)
    @ApiResponse(responseCode = "201", description = RESPONSE_201_DESCRIPTION, content = @Content)
    @StandardApiResponses
    @PostMapping("/")
    @PreAuthorize("hasAuthority('ROLE_Propietario')")
    public ResponseEntity<Void> saveDish(@RequestBody DishRequestDto dishRequestDto){
        dishHandler.saveDish(dishRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = DISH_UPDATE_SUMMARY)
    @ApiResponse(responseCode = "200", description = RESPONSE_200_DESCRIPTION, content = @Content)
    @StandardApiResponses
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_Propietario')")
    public ResponseEntity<Void> updateDish(@PathVariable Long id, @RequestBody DishUpdateRequestDto dishUpdateRequestDto) {
        dishHandler.updateDish(id, dishUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = DISH_ENABLE_DISABLE_SUMMARY)
    @ApiResponse(responseCode = "200", description = RESPONSE_200_DESCRIPTION, content = @Content)
    @StandardApiResponses
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_Propietario')")
    public ResponseEntity<Void> enableDisableDish(@PathVariable Long id, @RequestBody DishEnableDisableRequestDto dishEnableDisableRequestDto) {
        dishHandler.enableDisableDish(id, dishEnableDisableRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = DISH_LIST_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_200_DESCRIPTION, content = @Content),
            @ApiResponse(responseCode = "401", description = RESPONSE_401_DESCRIPTION, content = @Content),
            @ApiResponse(responseCode = "404", description = RESPONSE_404_DESCRIPTION, content = @Content)
    })
    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaginationResponse<DishListResponseDto>> listDishes(
            @Parameter(description = RESTAURANT_ID_PARAM_DESCRIPTION) @RequestParam Long restaurantId,
            @Parameter(description = CATEGORY_ID_PARAM_DESCRIPTION) @RequestParam(required = false) Long categoryId,
            @Parameter(description = PAGE_PARAM_DESCRIPTION) @RequestParam(defaultValue = PAGE_DEFAULT_VALUE) int page,
            @Parameter(description = SIZE_PARAM_DESCRIPTION) @RequestParam(defaultValue = SIZE_DEFAULT_VALUE) int size
    ) {
        return ResponseEntity.ok(dishHandler.listDishes(restaurantId, categoryId, page, size));
    }
}
