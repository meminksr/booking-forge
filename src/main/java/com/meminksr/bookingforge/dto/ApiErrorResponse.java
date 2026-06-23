package com.meminksr.bookingforge.dto;

import java.time.ZonedDateTime;

public record ApiErrorResponse(
        ZonedDateTime timestamp,
        int status,
        String error,
        String message
) {
}