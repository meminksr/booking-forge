package com.meminksr.bookingforge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meminksr.bookingforge.domain.Provider;
import com.meminksr.bookingforge.repository.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ProviderController Entegrasyon Testleri
 *
 * Tüm zinciri test eder: Controller → Security → Service → Repository → H2 DB
 * Provider endpoint'leri herkese açıktır (permitAll), JWT gerekmez.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProviderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Autowired
    private ProviderRepository providerRepository;

    // ==================== POST /api/v1/providers ====================

    @Test
    void createProvider_WithValidData_ShouldReturn201() throws Exception {
        Provider provider = new Provider();
        provider.setName("Dr. Ayşe Yılmaz");
        provider.setEmail("ayse@bookingforge.com");

        mockMvc.perform(post("/api/v1/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(provider)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Dr. Ayşe Yılmaz"))
                .andExpect(jsonPath("$.email").value("ayse@bookingforge.com"));
    }

    // ==================== GET /api/v1/providers ====================

    @Test
    void getAllProviders_WhenProvidersExist_ShouldReturnListAnd200() throws Exception {
        // Test verisi ekle
        Provider provider = new Provider();
        provider.setName("Ali Veli");
        provider.setEmail("ali@bookingforge.com");
        providerRepository.save(provider);

        mockMvc.perform(get("/api/v1/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(1)));
    }

    // ==================== GET /api/v1/providers/{id} ====================

    @Test
    void getProviderById_WhenExists_ShouldReturnProviderAnd200() throws Exception {
        // Test verisi ekle
        Provider provider = new Provider();
        provider.setName("Fatma Kaya");
        provider.setEmail("fatma@bookingforge.com");
        Provider saved = providerRepository.save(provider);

        mockMvc.perform(get("/api/v1/providers/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fatma Kaya"))
                .andExpect(jsonPath("$.email").value("fatma@bookingforge.com"));
    }

    @Test
    void getProviderById_WhenNotFound_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/providers/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
