package com.meminksr.bookingforge.service;

import com.meminksr.bookingforge.domain.Provider;
import com.meminksr.bookingforge.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
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
}