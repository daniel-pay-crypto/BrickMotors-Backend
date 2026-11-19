package com.brickmotors.ms_usuarios.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PedidoRequest(

        @NotNull(message = "El usuario es obligatorio.")
        Long usuarioId,

        @NotEmpty(message = "El carrito está vacío.")
        List<@Valid ItemPedidoRequest> items
) {
}