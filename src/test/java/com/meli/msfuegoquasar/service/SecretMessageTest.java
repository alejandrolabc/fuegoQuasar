package com.meli.msfuegoquasar.service;

import com.meli.msfuegoquasar.exceptions.BadRequestException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class SecretMessageTest {

    @InjectMocks
    private SecretMessage secretMessage;

    List<String> message = new ArrayList<>();

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        MockMvcBuilders.standaloneSetup(secretMessage).build();
        message.add("");
        message.add("es");
        message.add("");
        message.add("");
        message.add("secreto");

    }

    @Test
    void getMessage() {
        List<List<String>> msgSend = new ArrayList<>();
        msgSend.add(message);
        Assertions.assertNotNull(secretMessage.getMessage(msgSend));
    }

    @Test
    void defaultGetMessageMethodThrowsException() throws Exception {
        try {
            List<List<String>> msgSend = new ArrayList<>();
            secretMessage.defaultGetMessage(msgSend, new Exception());
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof BadRequestException);
        }
    }

    @Test
    void defaultGetMessageMethodReturnsNull() throws Exception {
        List<List<String>> msgSend = new ArrayList<>();
        msgSend.add(message);
        String response = secretMessage.defaultGetMessage(msgSend, new HystrixTimeoutException());
        Assertions.assertNull(response);
    }
}