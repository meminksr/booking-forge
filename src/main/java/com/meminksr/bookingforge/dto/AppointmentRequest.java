package com.meminksr.bookingforge.dto;

import jakarta.validation.constraints.*;
import java.time.ZonedDateTime;

import java.util.UUID;

public record AppointmentRequest(

        @NotNull(message = "Sağlayıcı ID boş olamaz.")
        UUID providerId,

        @NotBlank(message = "Müşteri adı boş olamaz.")
        @Size(min = 2, max = 100, message = "Müşteri adı en az 2, en fazla 100 karakter olmalıdır.")
        String clientName,

        @NotBlank(message = "Müşteri e-posta adresi boş olamaz.")
        @Email(message = "Geçerli bir e-posta adresi giriniz.")
        @Size(max = 150, message = "E-posta adresi en fazla 150 karakter olabilir.")
        String clientEmail,

        @NotNull(message = "Başlangıç zamanı boş olamaz.")
        @Future(message = "Başlangıç zamanı gelecekte bir tarih olmalıdır.")
        ZonedDateTime startTime,

        @NotNull(message = "Bitiş zamanı boş olamaz.")
        @Future(message = "Bitiş zamanı gelecekte bir tarih olmalıdır.")
        ZonedDateTime endTime
) {}