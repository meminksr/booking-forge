package com.meminksr.bookingforge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meminksr.bookingforge.domain.Provider;
import com.meminksr.bookingforge.domain.Role;
import com.meminksr.bookingforge.dto.AvailabilityRequest;
import com.meminksr.bookingforge.dto.RegisterRequest;
import com.meminksr.bookingforge.repository.ProviderRepository;
import com.meminksr.bookingforge.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AvailabilityController Entegrasyon Testleri
 *
 * Tüm zinciri test eder: Controller → Security (JWT) → Service → Repository → H2 DB
 * Availability endpoint'leri korumalıdır, JWT token gerektirir.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AvailabilityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private AuthService authService;

    private String jwtToken;
    private Provider testProvider;

    @BeforeEach
    void setUp() {
        // Test için bir kullanıcı oluştur ve JWT token al
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("avail-test-" + System.nanoTime() + "@bookingforge.com");
        registerRequest.setPassword("testPassword123");
        registerRequest.setRole(Role.PROVIDER);
        jwtToken = authService.register(registerRequest).getToken();

        // Test için bir provider oluştur
        testProvider = new Provider();
        testProvider.setName("Test Sağlayıcı");
        testProvider.setEmail("provider-" + System.nanoTime() + "@bookingforge.com");
        testProvider = providerRepository.save(testProvider);
    }

    // ==================== POST /api/v1/availabilities ====================

    @Test
    void addAvailability_WithAuth_ShouldReturn201() throws Exception {
        ZonedDateTime start = ZonedDateTime.now().plusDays(1).withHour(9).withMinute(0);
        ZonedDateTime end = start.plusHours(8);

        AvailabilityRequest request = new AvailabilityRequest(
                testProvider.getId(), start, end
        );

        mockMvc.perform(post("/api/v1/availabilities")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.provider.id").value(testProvider.getId()));
    }

    @Test
    void addAvailability_WithoutAuth_ShouldReturn403() throws Exception {
        ZonedDateTime start = ZonedDateTime.now().plusDays(1).withHour(9).withMinute(0);
        ZonedDateTime end = start.plusHours(8);

        AvailabilityRequest request = new AvailabilityRequest(
                testProvider.getId(), start, end
        );

        // JWT token olmadan istek at → 403 Forbidden beklenir
        mockMvc.perform(post("/api/v1/availabilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void addAvailability_WithInvalidTime_ShouldReturn400() throws Exception {
        // Bitiş saati, başlangıç saatinden ÖNCE → iş kuralı ihlali
        ZonedDateTime start = ZonedDateTime.now().plusDays(1).withHour(17).withMinute(0);
        ZonedDateTime end = start.minusHours(8); // bitiş < başlangıç!

        AvailabilityRequest request = new AvailabilityRequest(
                testProvider.getId(), start, end
        );

        mockMvc.perform(post("/api/v1/availabilities")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addAvailability_WithNonExistentProvider_ShouldReturn400() throws Exception {
        ZonedDateTime start = ZonedDateTime.now().plusDays(1).withHour(9).withMinute(0);
        ZonedDateTime end = start.plusHours(8);

        AvailabilityRequest request = new AvailabilityRequest(
                99999L, start, end
        );

        mockMvc.perform(post("/api/v1/availabilities")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ==================== GET /api/v1/availabilities/provider/{providerId} ====================

    @Test
    void getByProviderId_WithAuth_ShouldReturnListAnd200() throws Exception {
        mockMvc.perform(get("/api/v1/availabilities/provider/{providerId}", testProvider.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
