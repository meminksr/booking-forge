package com.meminksr.bookingforge.exception;

// RuntimeException'dan miras alıyoruz ki Spring Boot bunun bir hata durumu olduğunu anlasın
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}