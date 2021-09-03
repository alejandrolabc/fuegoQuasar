package com.meli.msfuegoquasar.application;

import com.meli.msfuegoquasar.dto.LocationDto;
import com.meli.msfuegoquasar.dto.SatellitesDto;
import com.meli.msfuegoquasar.service.LocationTrilateration;
import com.meli.msfuegoquasar.service.SecretMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MessageIntersectionApplicationTest {

    @InjectMocks
    private MessageIntersectionApplication application;

    @Mock
    private LocationTrilateration locationService;
    @Mock

    private SecretMessage messageService;

    List<SatellitesDto> satellites = new ArrayList<>();

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        MockMvcBuilders.standaloneSetup(application).build();
        var satellite = new SatellitesDto();
        satellite.setName("kenobi");
        satellite.setDistance(100.0);
        satellite.setMessage(new ArrayList<>());
        satellites.add(satellite);
        satellite = new SatellitesDto();
        satellite.setName("skywalker");
        satellite.setDistance(115.5);
        satellite.setMessage(new ArrayList<>());
        satellites.add(satellite);
        satellite = new SatellitesDto();
        satellite.setName("sato");
        satellite.setDistance(142.7);
        satellite.setMessage(new ArrayList<>());
        satellites.add(satellite);
    }

    @Test
    void getMessageAndPosition1() {
        Mockito.when(locationService.getLocation(100.0, 115.5, 142.7))
                .thenReturn(new LocationDto(12.1, 10.2));
        List<List<String>> msgSend = new ArrayList<>();
        for (SatellitesDto s : satellites) {
            msgSend.add(s.getMessage());
        }
        Mockito.when(messageService.getMessage(msgSend)).thenReturn("mensaje");
        Assertions.assertNotNull(application.getMessageAndPosition(satellites));
    }

    @Test
    void getMessageAndPosition2() {
        Assertions.assertNotNull(application.getMessageAndPosition(satellites));
    }
}