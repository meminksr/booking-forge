package com.meminksr.bookingforge.controller;

import com.meminksr.bookingforge.domain.Appointment;
import com.meminksr.bookingforge.dto.AppointmentRequest;
import com.meminksr.bookingforge.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@Tag(name = "Randevu Yönetimi", description = "Müşterilerin randevu almasını sağlayan uç noktalar")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Yeni Randevu Oluştur", description = "Müşterinin seçtiği saat diliminde çakışma yoksa randevuyu onaylar ve sisteme kaydeder.")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody AppointmentRequest request) {
        Appointment savedAppointment = appointmentService.bookAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);
    }
}