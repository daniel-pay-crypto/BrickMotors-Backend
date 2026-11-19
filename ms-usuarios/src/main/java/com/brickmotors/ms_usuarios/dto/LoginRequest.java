package com.brickmotors.ms_usuarios.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "Ingresa un correo válido de @gmail.com.")
        String correo,

        @NotBlank(message = "La contraseña no puede estar vacía.")
        String contrasena
) {
}