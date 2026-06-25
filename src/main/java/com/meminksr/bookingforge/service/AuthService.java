package com.meminksr.bookingforge.service;

import com.meminksr.bookingforge.domain.Role;
import com.meminksr.bookingforge.domain.User;
import com.meminksr.bookingforge.dto.AuthResponse;
import com.meminksr.bookingforge.dto.LoginRequest;
import com.meminksr.bookingforge.dto.RegisterRequest;
import com.meminksr.bookingforge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.meminksr.bookingforge.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Hatalı şifre!");
        }

        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}