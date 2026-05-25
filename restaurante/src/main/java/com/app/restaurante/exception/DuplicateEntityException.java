package com.app.restaurante.exception;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String entityName, String field, Object value) {
        super(entityName + " already exists with " + field + ": '" + value + "'");
    }
}