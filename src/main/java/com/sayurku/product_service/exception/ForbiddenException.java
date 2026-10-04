package com.sayurku.product_service.exception;

// 403: sudah login, tapi role/cabangnya tidak boleh
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
