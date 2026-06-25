package com.meminksr.bookingforge.controller;

import com.meminksr.bookingforge.domain.Provider;
import com.meminksr.bookingforge.repository.ProviderRepository;
import com.meminksr.bookingforge.service.ProviderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/providers")
@Tag(name = "Provider Management")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderRepository providerRepository;
    private final ProviderService providerService;

    @PostMapping
    public ResponseEntity<Provider> createProvider(@RequestBody Provider provider) {
        return ResponseEntity.status(201).body(providerService.save(provider));
    }

    @GetMapping
    public ResponseEntity<List<Provider>> getAllProviders() {
        return ResponseEntity.ok(providerService.getAllProviders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Provider> getProviderById(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.getProviderById(id));
    }


}
