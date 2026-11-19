package com.brickmotors.ms_usuarios.dto;

public record DetallePedidoResponse(
        Long productoId,
        String nombreProducto,
        Integer precioUnitario,
        Integer descuento,
        Integer cantidad,
        Integer subtotal
) {
}