package com.foodcourt.plaza_service.infrastructure.input.rest;

import com.foodcourt.plaza_service.application.dto.request.RestaurantRequestDto;
import com.foodcourt.plaza_service.application.dto.response.RestaurantListResponseDto;
import com.foodcourt.plaza_service.application.handler.IRestaurantHandler;
import com.foodcourt.plaza_service.domain.model.Page;
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
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantRestController {
    private final IRestaurantHandler restaurantHandler;

    @Operation(summary = RESTAURANT_CREATE_SUMMARY)
    @ApiResponse(responseCode = "201", description = RESTAURANT_CREATED_DESCRIPTION, content = @Content)
    @StandardApiResponses
    @PostMapping("/")
    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    public ResponseEntity<Void> saveRestaurant(@RequestBody RestaurantRequestDto restaurantRequestDto) {
        restaurantHandler.saveRestaurant(restaurantRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = RESTAURANT_LIST_SUMMARY)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESTAURANTS_LISTED_DESCRIPTION, content = @Content),
            @ApiResponse(responseCode = "401", description = RESPONSE_401_DESCRIPTION, content = @Content)
    })
    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<RestaurantListResponseDto>> listRestaurants(
            @Parameter(description = PAGE_PARAM_DESCRIPTION) @RequestParam(defaultValue = PAGE_DEFAULT_VALUE) int page,
            @Parameter(description = SIZE_PARAM_DESCRIPTION) @RequestParam(defaultValue = SIZE_DEFAULT_VALUE) int size
    ) {
        return ResponseEntity.ok(restaurantHandler.listRestaurants(page, size));
    }
}
