package com.foodcourt.plaza_service.infrastructure.input.rest;

import com.foodcourt.plaza_service.application.dto.request.OrderDeliverRequestDto;
import com.foodcourt.plaza_service.application.dto.request.OrderRequestDto;
import com.foodcourt.plaza_service.application.dto.response.OrderResponseDto;
import com.foodcourt.plaza_service.application.handler.IOrderHandler;
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
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @Operation(summary = "Realizar un pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado, rol incorrecto o no es cliente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurante o plato no encontrados", content = @Content)
    })
    @PostMapping("/")
    @PreAuthorize("hasAuthority('ROLE_Cliente')")
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        orderHandler.createOrder(orderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Listar pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos listados correctamente", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado, rol incorrecto o no es empleado", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontraron pedidos", content = @Content)
    })
    @GetMapping("/")
    @PreAuthorize("hasAuthority('ROLE_Empleado')")
    public ResponseEntity<Page<OrderResponseDto>> listOrdersByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(orderHandler.listOrdersByStatus(status, page, size));
    }

    @Operation(summary = "Asignarse a un pedido y cambiar estado a 'en preparación'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido asignado y actualizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado, rol incorrecto", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "El pedido no está en estado pendiente", content = @Content)
    })
    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasAuthority('ROLE_Empleado')")
    public ResponseEntity<Void> assignOrderToEmployee(@PathVariable Long id) {
        orderHandler.assignOrderToEmployee(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cambiar estado a 'listo' y mandar sms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado y mensaje enviado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado, rol incorrecto", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "El pedido no está en estado 'en preparación'", content = @Content)
    })
    @PatchMapping("/{id}/ready")
    @PreAuthorize("hasAuthority('ROLE_Empleado')")
    public ResponseEntity<Void> notifyOrderReady(@PathVariable Long id) {
        orderHandler.notifyOrderReady(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cambiar estado a 'Entregado'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado, rol incorrecto", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "El pedido no está en estado 'listo'", content = @Content)
    })
    @PatchMapping("/{id}/deliver")
    @PreAuthorize("hasAuthority('ROLE_Empleado')")
    public ResponseEntity<Void> deliverOrder(
            @PathVariable Long id,
            @RequestBody OrderDeliverRequestDto orderDeliverRequestDto) {
        orderHandler.deliverOrder(id, orderDeliverRequestDto);
        return ResponseEntity.ok().build();
    }
}
