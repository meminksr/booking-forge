package com.meminksr.bookingforge.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public record AvailabilityRequest(

        @NotNull(message = "Sağlayıcı ID boş olamaz.")
        Long providerId,

        @NotNull(message = "Başlangıç zamanı boş olamaz.")
        @Future(message = "Başlangıç zamanı gelecekte bir tarih olmalıdır.")
        ZonedDateTime startTime,

        @NotNull(message = "Bitiş zamanı boş olamaz.")
        @Future(message = "Bitiş zamanı gelecekte bir tarih olmalıdır.")
        ZonedDateTime endTime
) {}
