package com.policlinico.exception;

public class CitaNotFoundException extends RuntimeException {

    public CitaNotFoundException(String message) {
        super(message);
    }
}
