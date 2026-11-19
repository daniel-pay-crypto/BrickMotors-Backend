package com.brickmotors.ms_usuarios.dto;

public record ProductoResponse(
        Long id,
        String codigo,
        String nombre,
        String descripcion,
        Long categoriaId,
        String categoria,
        Integer precio,
        Integer stock,
        Integer descuento,
        boolean oferta,
        Integer precioFinal,
        String imagen
) {
}