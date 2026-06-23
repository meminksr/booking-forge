
package com.meminksr.bookingforge.repository;

import com.meminksr.bookingforge.domain.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    List<Availability> findByProviderIdAndStartTimeBetween(Long providerId, ZonedDateTime start, ZonedDateTime end);

    List<Availability> findByProviderId(Long providerId);
}
