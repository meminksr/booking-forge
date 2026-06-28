package com.meminksr.bookingforge.controller;

import com.meminksr.bookingforge.domain.Appointment;
import com.meminksr.bookingforge.dto.AppointmentRequest;
import com.meminksr.bookingforge.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@Tag(name = "Randevu Yönetimi", description = "Müşterilerin randevu almasını sağlayan uç noktalar")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Yeni Randevu Oluştur", description = "Müşterinin seçtiği saat diliminde çakışma yoksa randevuyu onaylar ve sisteme kaydeder.")
    public ResponseEntity<Appointment> bookAppointment(@Valid @RequestBody AppointmentRequest request) {
        Appointment savedAppointment = appointmentService.bookAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Appointment> cancelAppointment(
            @PathVariable Long id,
            Principal principal
    ) throws AccessDeniedException {
        String currentUserEmail = principal.getName();

        Appointment cancelledAppointment = appointmentService.cancelAppointment(id, currentUserEmail);
        return ResponseEntity.ok(cancelledAppointment);
    }
}