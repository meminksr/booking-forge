package com.meminksr.bookingforge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meminksr.bookingforge.domain.Role;
import com.meminksr.bookingforge.dto.LoginRequest;
import com.meminksr.bookingforge.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AuthController Entegrasyon Testleri
 *
 * Tüm zinciri test eder: Controller → Security → Service → Repository → H2 DB
 * Auth endpoint'leri herkese açıktır (permitAll), JWT gerekmez.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // ==================== REGISTER TESTLERİ ====================

    @Test
    void register_WithValidData_ShouldReturnTokenAnd200() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@bookingforge.com");
        request.setPassword("securePassword123");
        request.setRole(Role.USER);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    // ==================== LOGIN TESTLERİ ====================

    @Test
    void login_WithValidCredentials_ShouldReturnTokenAnd200() throws Exception {
        // Önce kullanıcı kaydet
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("login-test@bookingforge.com");
        registerRequest.setPassword("myPassword123");
        registerRequest.setRole(Role.USER);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // Sonra giriş yap
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("login-test@bookingforge.com");
        loginRequest.setPassword("myPassword123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void login_WithWrongPassword_ShouldReturnError() throws Exception {
        // Önce kullanıcı kaydet
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("wrong-pass@bookingforge.com");
        registerRequest.setPassword("correctPassword");
        registerRequest.setRole(Role.USER);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // Yanlış şifre ile giriş dene
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong-pass@bookingforge.com");
        loginRequest.setPassword("wrongPassword");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void login_WithNonExistentUser_ShouldReturnError() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("ghost@bookingforge.com");
        loginRequest.setPassword("anyPassword");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError());
    }
}
