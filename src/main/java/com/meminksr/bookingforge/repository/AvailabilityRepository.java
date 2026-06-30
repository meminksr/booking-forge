
package com.meminksr.bookingforge.repository;

import com.meminksr.bookingforge.domain.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

import java.util.UUID;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, UUID> {

    List<Availability> findByProviderIdAndStartTimeBetween(UUID providerId, ZonedDateTime start, ZonedDateTime end);

    List<Availability> findByProviderId(UUID providerId);
}
