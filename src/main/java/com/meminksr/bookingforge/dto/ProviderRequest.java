package com.meminksr.bookingforge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProviderRequest(

        @NotBlank(message = "Sağlayıcı adı boş olamaz.")
        @Size(min = 2, max = 100, message = "Sağlayıcı adı en az 2, en fazla 100 karakter olmalıdır.")
        String name,

        @NotBlank(message = "E-posta adresi boş olamaz.")
        @Email(message = "Geçerli bir e-posta adresi giriniz.")
        @Size(max = 150, message = "E-posta adresi en fazla 150 karakter olabilir.")
        String email
) {}
