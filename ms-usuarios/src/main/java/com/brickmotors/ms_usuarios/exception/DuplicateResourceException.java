package com.brickmotors.ms_usuarios.exception;

import lombok.Getter;

/** Valor repetido en un campo único (correo, código, nombre de categoría). */
@Getter
public class DuplicateResourceException extends RuntimeException {

    private final String campo;

    public DuplicateResourceException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }
}