package com.brickmotors.ms_usuarios.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nombre,
        String correo,
        String rol,
        boolean esAdmin,
        boolean activo,
        LocalDateTime fechaRegistro
) {
}