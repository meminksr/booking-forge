package com.meminksr.bookingforge.controller;

import com.meminksr.bookingforge.domain.Availability;
import com.meminksr.bookingforge.dto.AvailabilityRequest;
import com.meminksr.bookingforge.service.AvailabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/availabilities")
@RequiredArgsConstructor
@Tag(name = "Müsaitlik Yönetimi", description = "Hizmet sağlayıcıların çalışma saatlerini (slotlarını) belirleme uç noktaları")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping
    @Operation(summary = "Yeni Müsaitlik Ekle", description = "Sağlayıcının müsait olduğu yeni bir zaman dilimi oluşturur.")
    public ResponseEntity<Availability> addAvailability(@Valid @RequestBody AvailabilityRequest request) {
        Availability savedAvailability = availabilityService.addAvailability(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAvailability);
    }


    // Endpoint: /api/v1/availabilities/provider/{providerId}
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<Availability>> getAvailabilitiesByProviderId(@PathVariable UUID providerId) {
        return ResponseEntity.ok(availabilityService.getAvailabilitiesByProviderId(providerId));
    }
}