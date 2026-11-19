package com.brickmotors.ms_usuarios.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductoRequest(

        @NotBlank(message = "El código no puede estar vacío.")
        @Size(max = 20, message = "El código no puede superar los 20 caracteres.")
        String codigo,

        @NotBlank(message = "El nombre no puede estar vacío.")
        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres.")
        String nombre,

        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres.")
        String descripcion,

        @NotNull(message = "Debes seleccionar una categoría.")
        Long categoriaId,

        @NotNull(message = "El precio no puede estar vacío.")
        @Positive(message = "El precio debe ser mayor a 0.")
        Integer precio,

        @NotNull(message = "El stock no puede estar vacío.")
        @PositiveOrZero(message = "El stock debe ser un número entero, 0 o mayor.")
        Integer stock,

        @Min(value = 0, message = "El descuento debe ser un número entero entre 0 y 90.")
        @Max(value = 90, message = "El descuento debe ser un número entero entre 0 y 90.")
        Integer descuento,

        @Size(max = 500, message = "La URL de la imagen no puede superar los 500 caracteres.")
        String imagen
) {
}