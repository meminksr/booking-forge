package com.meminksr.bookingforge.dto;

import com.meminksr.bookingforge.domain.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;
}