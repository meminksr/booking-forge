package com.meminksr.bookingforge.dto;

import java.time.ZonedDateTime;

public record AppointmentRequest(
        Long providerId,
        String clientName,
        String clientEmail,
        ZonedDateTime startTime,
        ZonedDateTime endTime
) {}