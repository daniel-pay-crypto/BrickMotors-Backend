package com.brickmotors.ms_usuarios.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.brickmotors.ms_usuarios.model.EstadoPedido;

public record PedidoResponse(
        Long id,
        Long usuarioId,
        String nombreUsuario,
        LocalDateTime fecha,
        EstadoPedido estado,
        Integer total,
        List<DetallePedidoResponse> detalles
) {
}