package com.foodcourt.plaza_service.infrastructure.input.rest;

import com.foodcourt.plaza_service.application.dto.request.OrderDeliverRequestDto;
import com.foodcourt.plaza_service.application.dto.request.OrderRequestDto;
import com.foodcourt.plaza_service.application.dto.response.OrderResponseDto;
import com.foodcourt.plaza_service.application.handler.IOrderHandler;
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
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @Operation(summary = ORDER_CREATE_SUMMARY)
    @ApiResponse(responseCode = "201", description = RESPONSE_201_DESCRIPTION, content = @Content)
    @StandardApiResponses
    @PostMapping("/")
    @PreAuthorize("hasAuthority('ROLE_Cliente')")
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        orderHandler.createOrder(orderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = ORDER_LIST_SUMMARY)
    @ApiResponse(responseCode = "200", description = RESPONSE_200_DESCRIPTION, content = @Content)
    @StandardApiResponses
    @GetMapping("/")
    @PreAuthorize("hasAuthority('ROLE_Empleado')")
    public ResponseEntity<Page<OrderResponseDto>> listOrdersByStatus(
            @Parameter(description = STATUS_PARAM_DESCRIPTION) @RequestParam String status,
            @Parameter(description = PAGE_PARAM_DESCRIPTION) @RequestParam(defaultValue = PAGE_DEFAULT_VALUE) int page,
            @Parameter(description = SIZE_PARAM_DESCRIPTION) @RequestParam(defaultValue = SIZE_DEFAULT_VALUE) int size
    ) {
        return ResponseEntity.ok(orderHandler.listOrdersByStatus(status, page, size));
    }

    @Operation(summary = ORDER_ASSIGN_SUMMARY)
    @ApiResponse(responseCode = "200", description = RESPONSE_200_DESCRIPTION, content = @Content)
    @StandardApiResponses
    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasAuthority('ROLE_Empleado')")
    public ResponseEntity<Void> assignOrderToEmployee(@PathVariable Long id) {
        orderHandler.assignOrderToEmployee(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = ORDER_READY_SUMMARY)
    @ApiResponse(responseCode = "200", description = RESPONSE_200_DESCRIPTION, content = @Content)
    @StandardApiResponses
    @PatchMapping("/{id}/ready")
    @PreAuthorize("hasAuthority('ROLE_Empleado')")
    public ResponseEntity<Void> notifyOrderReady(@PathVariable Long id) {
        orderHandler.notifyOrderReady(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = ORDER_DELIVER_SUMMARY)
    @ApiResponse(responseCode = "200", description = RESPONSE_200_DESCRIPTION, content = @Content)
    @StandardApiResponses
    @PatchMapping("/{id}/deliver")
    @PreAuthorize("hasAuthority('ROLE_Empleado')")
    public ResponseEntity<Void> deliverOrder(@PathVariable Long id, @RequestBody OrderDeliverRequestDto orderDeliverRequestDto) {
        orderHandler.deliverOrder(id, orderDeliverRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = ORDER_CANCEL_SUMMARY)
    @ApiResponse(responseCode = "200", description = RESPONSE_200_DESCRIPTION, content = @Content)
    @StandardApiResponses
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('ROLE_Cliente')")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderHandler.cancelOrder(id);
        return ResponseEntity.ok().build();
    }
}
