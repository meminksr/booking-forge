package com.meminksr.bookingforge.dto;

import java.time.ZonedDateTime;
import java.util.Map;

public record ApiErrorResponse(
        ZonedDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors
) {
    public ApiErrorResponse(ZonedDateTime timestamp, int status, String error, String message) {
        this(timestamp, status, error, message, null);
    }
}