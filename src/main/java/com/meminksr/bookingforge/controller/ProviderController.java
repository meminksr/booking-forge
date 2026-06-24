package com.meminksr.bookingforge.controller;

import com.meminksr.bookingforge.domain.Provider;
import com.meminksr.bookingforge.repository.ProviderRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/providers")
@Tag(name = "Provider Management")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderRepository providerRepository;

    @PostMapping
    public Provider createProvider(@RequestBody Provider provider) {
        return providerRepository.save(provider);
    }

    @GetMapping
    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }
}