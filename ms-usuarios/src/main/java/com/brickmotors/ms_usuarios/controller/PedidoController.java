package com.brickmotors.ms_usuarios.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brickmotors.ms_usuarios.dto.EstadoPedidoRequest;
import com.brickmotors.ms_usuarios.dto.PedidoRequest;
import com.brickmotors.ms_usuarios.dto.PedidoResponse;
import com.brickmotors.ms_usuarios.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Compras realizadas desde el carrito")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @Operation(summary = "Crear un pedido a partir del carrito")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido creado y stock descontado"),
            @ApiResponse(responseCode = "400", description = "Carrito inválido"),
            @ApiResponse(responseCode = "404", description = "Usuario o producto no existe"),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente")
    })
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody PedidoRequest request) {
        PedidoResponse creado = pedidoService.crear(request);
        return ResponseEntity.created(URI.create("/api/v1/pedidos/" + creado.id())).body(creado);
    }

    @GetMapping
    @Operation(summary = "Listar pedidos (opcional: ?usuarioId= para 'Mis pedidos')")
    public ResponseEntity<List<PedidoResponse>> listar(
            @RequestParam(name = "usuarioId", required = false) Long usuarioId) {
        return ResponseEntity.ok(pedidoService.listar(usuarioId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pedido con su detalle")
    public ResponseEntity<PedidoResponse> obtener(@PathVariable("id") Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar el estado de un pedido (CANCELADO devuelve el stock)")
    public ResponseEntity<PedidoResponse> cambiarEstado(@PathVariable("id") Long id,
                                                        @Valid @RequestBody EstadoPedidoRequest request) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, request.estado()));
    }
}