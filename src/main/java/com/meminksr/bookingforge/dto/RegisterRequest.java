package com.meminksr.bookingforge.dto;

import com.meminksr.bookingforge.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "E-posta adresi boş olamaz.")
    @Email(message = "Geçerli bir e-posta adresi giriniz.")
    @Size(max = 150, message = "E-posta adresi en fazla 150 karakter olabilir.")
    private String email;

    @NotBlank(message = "Şifre boş olamaz.")
    @Size(min = 6, max = 100, message = "Şifre en az 6, en fazla 100 karakter olmalıdır.")
    private String password;

    @NotNull(message = "Rol bilgisi boş olamaz. (USER, ADMIN veya PROVIDER)")
    private Role role;
}