package com.brickmotors.ms_usuarios.dto;

import java.time.LocalDateTime;

public record MensajeContactoResponse(
        Long id,
        String nombre,
        String correo,
        String mensaje,
        LocalDateTime fecha,
        boolean leido
) {
}