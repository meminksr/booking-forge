package com.meminksr.bookingforge.service;

import com.meminksr.bookingforge.domain.Provider;
import com.meminksr.bookingforge.exception.ResourceNotFoundException;
import com.meminksr.bookingforge.repository.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository; // Sahte (Dublör) Veritabanımız

    @InjectMocks
    private ProviderService providerService; // Test Edeceğimiz GERÇEK Servis

    @Test
    void getProviderById_WhenIdExists_ShouldReturnProvider() {

        Long providerId = 1L;
        Provider mockProvider = new Provider();

        // Dublör veritabanımıza emir veriyoruz: "Eğer sana 1 ID'si gelirse, bu mockProvider'ı bulmuşsun gibi yap!"
        when(providerRepository.findById(providerId)).thenReturn(Optional.of(mockProvider));


        Provider result = providerService.getProviderById(providerId);


        assertNotNull(result); // Gelen sonuç kesinlikle null olmamalı
        verify(providerRepository, times(1)).findById(providerId); // Veritabanına gerçekten tam 1 kez gidildi mi?
    }

    @Test
    void getProviderById_WhenIdDoesNotExist_ShouldThrowResourceNotFoundException() {

        Long missingId = 1L;

        // Dublör veritabanına emir veriyoruz: "O ID bende yok de (Boş dön)."
        when(providerRepository.findById(missingId)).thenReturn(Optional.empty());


        // Olmayan bir ID'yi aradığımızda sistemin o bizim yazdığımız ÖZEL HATAYI fırlattığını doğrula!
        assertThrows(ResourceNotFoundException.class, () -> {
            providerService.getProviderById(missingId);
        });


        // Veritabanında arama işlemi gerçekten 1 kez yapılmış mı?
        verify(providerRepository, times(1)).findById(missingId);

    }

}