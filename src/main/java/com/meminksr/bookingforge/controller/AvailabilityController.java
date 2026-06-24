package com.meminksr.bookingforge.controller;

import com.meminksr.bookingforge.domain.Availability;
import com.meminksr.bookingforge.dto.AvailabilityRequest;
import com.meminksr.bookingforge.service.AvailabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/availabilities")
@RequiredArgsConstructor
@Tag(name = "Müsaitlik Yönetimi", description = "Hizmet sağlayıcıların çalışma saatlerini (slotlarını) belirleme uç noktaları")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping
    @Operation(summary = "Yeni Müsaitlik Ekle", description = "Sağlayıcının müsait olduğu yeni bir zaman dilimi oluşturur.")
    public ResponseEntity<?> addAvailability(@RequestBody AvailabilityRequest request) {
        try {
            Availability savedAvailability = availabilityService.addAvailability(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAvailability);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sistemsel bir hata oluştu.");
        }
    }
    // Endpoint: /api/v1/availabilities/provider/{providerId}
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<Availability>> getAvailabilitiesByProviderId(@PathVariable Long providerId) {
        return ResponseEntity.ok(availabilityService.getAvailabilitiesByProviderId(providerId));
    }
}