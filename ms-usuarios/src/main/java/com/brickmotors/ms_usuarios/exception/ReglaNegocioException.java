package com.brickmotors.ms_usuarios.exception;

/** Operación válida en formato pero no permitida (sin stock, pedido ya cancelado, etc.). */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}