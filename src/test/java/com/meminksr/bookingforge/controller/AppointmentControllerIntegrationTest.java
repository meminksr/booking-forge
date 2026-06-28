package com.meminksr.bookingforge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meminksr.bookingforge.domain.Availability;
import com.meminksr.bookingforge.domain.Provider;
import com.meminksr.bookingforge.domain.Role;
import com.meminksr.bookingforge.dto.AppointmentRequest;
import com.meminksr.bookingforge.dto.RegisterRequest;
import com.meminksr.bookingforge.repository.AvailabilityRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AppointmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private AuthService authService;

    private String jwtToken;
    private Provider testProvider;
    private ZonedDateTime workStart;
    private ZonedDateTime workEnd;

    @BeforeEach
    void setUp() {
        // JWT token al
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("appt-test-" + System.nanoTime() + "@bookingforge.com");
        registerRequest.setPassword("testPassword123");
        registerRequest.setRole(Role.USER);
        jwtToken = authService.register(registerRequest).getToken();

        // Test provider oluştur
        testProvider = new Provider();
        testProvider.setName("Dr. Test Doktor");
        testProvider.setEmail("doctor-" + System.nanoTime() + "@bookingforge.com");
        testProvider = providerRepository.save(testProvider);

        workStart = ZonedDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
        workEnd = workStart.withHour(17);

        Availability availability = new Availability();
        availability.setProvider(testProvider);
        availability.setStartTime(workStart);
        availability.setEndTime(workEnd);
        availabilityRepository.save(availability);
    }

    // ==================== POST /api/v1/appointments ====================

    @Test
    void bookAppointment_WithAuth_ShouldReturn201() throws Exception {
        // Çalışma saatleri içinde, çakışmayan bir randevu
        AppointmentRequest request = new AppointmentRequest(
                testProvider.getId(),
                "Ahmet Yılmaz",
                "ahmet@gmail.com",
                workStart.plusHours(1),    // 10:00
                workStart.plusHours(2)     // 11:00
        );

        mockMvc.perform(post("/api/v1/appointments")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.clientName").value("Ahmet Yılmaz"))
                .andExpect(jsonPath("$.clientEmail").value("ahmet@gmail.com"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void bookAppointment_WithoutAuth_ShouldReturn403() throws Exception {
        AppointmentRequest request = new AppointmentRequest(
                testProvider.getId(),
                "Mehmet Kara",
                "mehmet@gmail.com",
                workStart.plusHours(1),
                workStart.plusHours(2)
        );

        // JWT token olmadan istek at → 403 Forbidden beklenir
        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void bookAppointment_ProviderNotFound_ShouldReturn400() throws Exception {
        AppointmentRequest request = new AppointmentRequest(
                99999L,  // olmayan provider ID
                "Zeynep Demir",
                "zeynep@gmail.com",
                workStart.plusHours(1),
                workStart.plusHours(2)
        );

        mockMvc.perform(post("/api/v1/appointments")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bookAppointment_OutsideWorkingHours_ShouldReturn400() throws Exception {
        // Çalışma saatleri dışında randevu → 17:00 - 18:00 (provider 09-17 çalışıyor)
        AppointmentRequest request = new AppointmentRequest(
                testProvider.getId(),
                "Can Yücel",
                "can@gmail.com",
                workEnd.plusHours(1),       // 18:00
                workEnd.plusHours(2)        // 19:00
        );

        mockMvc.perform(post("/api/v1/appointments")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bookAppointment_OverlappingTime_ShouldReturn400() throws Exception {
        // İlk randevuyu al: 10:00 - 11:00
        AppointmentRequest firstRequest = new AppointmentRequest(
                testProvider.getId(),
                "İlk Müşteri",
                "ilk@gmail.com",
                workStart.plusHours(1),    // 10:00
                workStart.plusHours(2)     // 11:00
        );

        mockMvc.perform(post("/api/v1/appointments")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isCreated());

        // Aynı saat dilimine ikinci randevu dene: 10:30 - 11:30 → ÇAKIŞMA!
        AppointmentRequest overlappingRequest = new AppointmentRequest(
                testProvider.getId(),
                "İkinci Müşteri",
                "ikinci@gmail.com",
                workStart.plusHours(1).plusMinutes(30),  // 10:30
                workStart.plusHours(2).plusMinutes(30)   // 11:30
        );

        mockMvc.perform(post("/api/v1/appointments")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(overlappingRequest)))
                .andExpect(status().isBadRequest());
    }

    // ==================== VALIDATION TESTS ====================

    @Test
    void bookAppointment_WithInvalidEmail_ShouldReturn400() throws Exception {
        AppointmentRequest request = new AppointmentRequest(
                testProvider.getId(),
                "Ahmet Yılmaz",
                "invalid-email", // Geçersiz e-posta
                workStart.plusHours(1),
                workStart.plusHours(2)
        );

        mockMvc.perform(post("/api/v1/appointments")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.clientEmail").isNotEmpty());
    }

    @Test
    void bookAppointment_WithPastTime_ShouldReturn400() throws Exception {
        AppointmentRequest request = new AppointmentRequest(
                testProvider.getId(),
                "Ahmet Yılmaz",
                "ahmet@gmail.com",
                ZonedDateTime.now().minusDays(1), // Geçmiş zaman
                ZonedDateTime.now().minusDays(1).plusHours(1)
        );

        mockMvc.perform(post("/api/v1/appointments")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.startTime").isNotEmpty())
                .andExpect(jsonPath("$.fieldErrors.endTime").isNotEmpty());
    }
}
