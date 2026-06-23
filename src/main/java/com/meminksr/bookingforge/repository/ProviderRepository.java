package com.meminksr.bookingforge.repository;


import com.meminksr.bookingforge.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    // Aynı e-posta ile kayıt olunmasını engellemek için kontrol metodu
    boolean existsByEmail(String email);
}
