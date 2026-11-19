package com.brickmotors.ms_usuarios.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String mensaje;
    private String detalle;
    private Integer status;
    private LocalDateTime timestamp;
    /** Errores por campo para mostrarlos bajo cada input en React. */
    private Map<String, String> errores;
}