package com.meminksr.bookingforge.repository;


import com.meminksr.bookingforge.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    // çakışma kontrolü
    List<Appointment> findByProviderIdAndStartTimeBetween(UUID providerId, ZonedDateTime start, ZonedDateTime end);
}
