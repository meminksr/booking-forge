package com.meminksr.bookingforge.repository;


import com.meminksr.bookingforge.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // çakışma kontrolü
    List<Appointment> findByProviderIdAndStartTimeBetween(Long providerId, ZonedDateTime start, ZonedDateTime end);
}
