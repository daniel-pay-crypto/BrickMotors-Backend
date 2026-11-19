package com.brickmotors.ms_usuarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MensajeContactoRequest(

        @NotBlank(message = "El nombre no puede estar vacío.")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
        String nombre,

        @NotBlank(message = "Ingresa un correo válido de @gmail.com.")
        @Pattern(regexp = "^(?i)(?=[^@]{3,}@)(?=[^@]*[a-z])[a-z0-9]+([._%+-][a-z0-9]+)*@gmail\\.com$",
                 message = "Ingresa un correo válido de @gmail.com.")
        String correo,

        @NotBlank(message = "El mensaje no puede estar vacío.")
        @Size(max = 1000, message = "El mensaje no puede superar los 1000 caracteres.")
        String mensaje
) {
}