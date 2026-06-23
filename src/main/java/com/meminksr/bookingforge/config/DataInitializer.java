package com.meminksr.bookingforge.config;

import com.meminksr.bookingforge.domain.Provider;
import com.meminksr.bookingforge.repository.ProviderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(ProviderRepository repository) {
        return args -> {
            // Sistemde hiç sağlayıcı yoksa 1 tane ekle
            if (repository.count() == 0) {
                Provider provider = new Provider();
                provider.setName("Mehmet Keser Danışmanlık");
                provider.setEmail("iletisim@mehmetkeser.com");
                repository.save(provider);
                System.out.println("TEST VERİSİ EKLENDİ: ID = " + provider.getId());
            }
        };
    }
}