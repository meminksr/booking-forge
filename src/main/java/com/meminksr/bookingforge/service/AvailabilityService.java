package com.meminksr.bookingforge.service;

import com.meminksr.bookingforge.domain.Availability;
import com.meminksr.bookingforge.domain.Provider;
import com.meminksr.bookingforge.dto.AvailabilityRequest;
import com.meminksr.bookingforge.repository.AvailabilityRepository;
import com.meminksr.bookingforge.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final ProviderRepository providerRepository;

    @Transactional
    public Availability addAvailability(AvailabilityRequest request) {

        if (!request.endTime().isAfter(request.startTime())) {
            throw new IllegalArgumentException("Bitiş saati, başlangıç saatinden daima sonra olmalıdır!");
        }

        Provider provider = providerRepository.findById(request.providerId())
                .orElseThrow(() -> new IllegalArgumentException("Hizmet sağlayıcı bulunamadı!"));

        Availability availability = new Availability();
        availability.setProvider(provider);

        // ZonedDateTime sayesinde saat dilimi fark etmeksizin evrensel bir anı kaydediyoruz
        availability.setStartTime(request.startTime());
        availability.setEndTime(request.endTime());

        return availabilityRepository.save(availability);
    }
    public List<Availability> getAvailabilitiesByProviderId(Long providerId) {
        return availabilityRepository.findByProviderId(providerId);
    }
}
