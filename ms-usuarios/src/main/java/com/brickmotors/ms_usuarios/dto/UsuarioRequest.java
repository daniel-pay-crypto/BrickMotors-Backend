package com.brickmotors.ms_usuarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(

        @NotBlank(message = "El nombre no puede estar vacío.")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
        @Pattern(regexp = "^(?iu)(?!.*(.)\\1\\1)[a-záéíóúüñ]+([ '-][a-záéíóúüñ]+)*$",
                 message = "Ingresa un nombre válido: solo letras y sin repetir una letra 3 veces seguidas.")
        String nombre,

        @NotBlank(message = "Ingresa un correo válido de @gmail.com.")
        @Size(max = 255, message = "El correo no puede superar los 255 caracteres.")
        @Pattern(regexp = "^(?i)(?=[^@]{3,}@)(?=[^@]*[a-z])[a-z0-9]+([._%+-][a-z0-9]+)*@gmail\\.com$",
                 message = "Ingresa un correo válido de @gmail.com.")
        String correo,

        @NotBlank(message = "La contraseña no puede estar vacía.")
        @Size(max = 72, message = "La contraseña no puede superar los 72 caracteres.")
        @Pattern(regexp = "^(?=\\S{8,}$)(?=.*[A-ZÁÉÍÓÚÑ])(?=.*[a-záéíóúñ])(?=.*[0-9])(?=.*[^A-Za-z0-9ÁÉÍÓÚÑáéíóúñ\\s]).*$",
                 message = "La contraseña debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.")
        String contrasena
) {
}