package com.brickmotors.ms_usuarios.dto;

import com.brickmotors.ms_usuarios.model.EstadoPedido;

import jakarta.validation.constraints.NotNull;

public record EstadoPedidoRequest(

        @NotNull(message = "El estado es obligatorio.")
        EstadoPedido estado
) {
}