package com.responsys.data.exception;

public class ContactNotFoundException extends RuntimeException {
    public ContactNotFoundException(Long id) {
        super("Contacto no encontrado con id: " + id);
    }
}
