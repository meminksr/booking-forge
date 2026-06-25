package com.meminksr.bookingforge.service;

import com.meminksr.bookingforge.domain.Provider;
import com.meminksr.bookingforge.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderService {
    private final ProviderRepository providerRepository;

    public Provider save(Provider provider) {
        return providerRepository.save(provider);
    }
    public List<Provider> findAll() {
        return providerRepository.findAll();
    }

    public Provider getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new com.meminksr.bookingforge.exception.ResourceNotFoundException("Belirtilen ID ile eşleşen sağlayıcı bulunamadı: " + id));
    }

    public @Nullable List<Provider> getAllProviders() {
        List<Provider> providers = providerRepository.findAll();
        if (providers.isEmpty()) {
            return null;
        }
        return providers;
    }
}


