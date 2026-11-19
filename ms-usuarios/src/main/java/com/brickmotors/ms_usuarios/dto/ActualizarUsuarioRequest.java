package com.brickmotors.ms_usuarios.dto;

import com.brickmotors.ms_usuarios.model.Rol;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ActualizarUsuarioRequest(

        @NotBlank(message = "El nombre no puede estar vacío.")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
        @Pattern(regexp = "^(?iu)(?!.*(.)\\1\\1)[a-záéíóúüñ]+([ '-][a-záéíóúüñ]+)*$",
                 message = "Ingresa un nombre válido: solo letras y sin repetir una letra 3 veces seguidas.")
        String nombre,

        @NotBlank(message = "Ingresa un correo válido de @gmail.com.")
        @Pattern(regexp = "^(?i)(?=[^@]{3,}@)(?=[^@]*[a-z])[a-z0-9]+([._%+-][a-z0-9]+)*@gmail\\.com$",
                 message = "Ingresa un correo válido de @gmail.com.")
        String correo,

        @NotNull(message = "Debes seleccionar un tipo de usuario.")
        Rol rol
) {
}