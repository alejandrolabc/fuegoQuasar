package com.meli.msfuegoquasar.service;

import com.meli.msfuegoquasar.dto.LocationDto;
import com.meli.msfuegoquasar.exceptions.BadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LocationTrilaterationTest {

    @InjectMocks
    private LocationTrilateration locationTrilateration;

    @Test
    void getLocation() {
        locationTrilateration.setKenobiLongitude("-500");
        locationTrilateration.setKenobiLatitude("-200");
        locationTrilateration.setSkywalkerLongitude("100");
        locationTrilateration.setSkywalkerLatitude("-100");
        locationTrilateration.setSatoLongitude("500");
        locationTrilateration.setSatoLatitude("100");
        double d1 = 100.0;
        double d2 = 120.0;
        double d3 = 150.0;
        Assertions.assertNotNull(locationTrilateration.getLocation(d1,d2, d3));
    }

    @Test
    void defaultGetLocationMethodThrowsException() throws Exception {
        try {
            locationTrilateration.defaultGetLocation(0.0, 0.0, 0.0, new Exception());
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof BadRequestException);
        }
    }

    @Test
    void defaultGetLocationMethodReturnsNull() throws Exception {
        double d1 = 100.0;
        double d2 = 120.0;
        double d3 = 150.0;
        LocationDto response = locationTrilateration.defaultGetLocation(d1, d2, d3, new HystrixTimeoutException());
        Assertions.assertNull(response);
    }
}