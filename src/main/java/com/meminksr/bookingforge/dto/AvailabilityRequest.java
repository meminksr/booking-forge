package com.meminksr.bookingforge.dto;


import java.time.ZonedDateTime;

public record AvailabilityRequest(
        Long providerId,
        ZonedDateTime startTime,
        ZonedDateTime endTime
) {
}
