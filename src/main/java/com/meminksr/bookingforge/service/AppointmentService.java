package com.meminksr.bookingforge.service;

import com.meminksr.bookingforge.domain.Appointment;
import com.meminksr.bookingforge.domain.AppointmentStatus;
import com.meminksr.bookingforge.domain.Availability;
import com.meminksr.bookingforge.domain.Provider;
import com.meminksr.bookingforge.dto.AppointmentRequest;
import com.meminksr.bookingforge.repository.AppointmentRepository;
import com.meminksr.bookingforge.repository.AvailabilityRepository;
import com.meminksr.bookingforge.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ProviderRepository providerRepository;
    private final AvailabilityRepository availabilityRepository;

    @Transactional
    public Appointment bookAppointment(AppointmentRequest request) {

        Provider provider = providerRepository.findById(request.providerId())
                .orElseThrow(() -> new IllegalArgumentException("Hizmet sağlayıcı bulunamadı!"));

        boolean isWorkingHours = checkAvailability(request.providerId(), request.startTime(), request.endTime());
        if (!isWorkingHours) {
            throw new IllegalStateException("Hizmet sağlayıcı seçtiğiniz saatler arasında çalışmıyor (Müsait değil).");
        }

        boolean hasOverlap = checkOverlap(request.providerId(), request.startTime(), request.endTime());
        if (hasOverlap) {
            throw new IllegalStateException("Seçilen zaman dilimi dolu, lütfen başka bir saat seçin.");
        }

        Appointment appointment = new Appointment();
        appointment.setProvider(provider);
        appointment.setClientName(request.clientName());
        appointment.setClientEmail(request.clientEmail());
        appointment.setStartTime(request.startTime());
        appointment.setEndTime(request.endTime());
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        return appointmentRepository.save(appointment);
    }

    private boolean checkAvailability(Long providerId, ZonedDateTime reqStart, ZonedDateTime reqEnd) {
        List<Availability> availabilities = availabilityRepository.findByProviderId(providerId);

        return availabilities.stream().anyMatch(avail ->
                (reqStart.isEqual(avail.getStartTime()) || reqStart.isAfter(avail.getStartTime())) &&
                        (reqEnd.isEqual(avail.getEndTime()) || reqEnd.isBefore(avail.getEndTime()))
        );
    }

    private boolean checkOverlap(Long providerId, ZonedDateTime reqStart, ZonedDateTime reqEnd) {
        List<Appointment> existingAppointments = appointmentRepository
                .findByProviderIdAndStartTimeBetween(providerId, reqStart.minusDays(1), reqEnd.plusDays(1));

        return existingAppointments.stream().anyMatch(existing ->
                reqStart.isBefore(existing.getEndTime()) && reqEnd.isAfter(existing.getStartTime())
        );
    }
}
