package com.meli.msfuegoquasar.application;

import com.meli.msfuegoquasar.dto.LocationDto;
import com.meli.msfuegoquasar.dto.ResponseDto;
import com.meli.msfuegoquasar.dto.SatellitesDto;
import com.meli.msfuegoquasar.service.LocationTrilateration;
import com.meli.msfuegoquasar.service.SecretMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/***
 * Clase componente que sirve para orquestar las dos clases servicio
 * @author Alejandro Balaguera
 *
 */
@Component
@Slf4j
public class MessageIntersectionApplication {

    @Autowired
    private LocationTrilateration locationService;
    @Autowired
    private SecretMessage messageService;

    /**
     * Metodo que recibe la información de los 3 satelites para
     * consumir el servicio getLocation y getMessage de los dos
     * servicios de la capa Service, para retornar la posicion y
     * el mensaje armado.
     *
     * @author Alejandro Balaguera
     * @param satellites, de tipo List SatellitesDto
     * @return {@link ResponseDto responseMessage}
     **/
    public ResponseDto getMessageAndPosition(List<SatellitesDto> satellites) {
        ResponseDto responseMessage = new ResponseDto();
        LocationDto position;
        //Declarar las 3 distancias
        var d1 = 0.0;
        var d2 = 0.0;
        var d3 = 0.0;
        //Se recorre la información de los satelites para asignar la
        //distancia de acuerdo al nombre del mismo
        for (SatellitesDto s : satellites) {
            switch (s.getName()) {
                case "kenobi":
                    d1 = s.getDistance();
                    break;
                case "skywalker":
                    d2 = s.getDistance();
                    break;
                case "sato":
                    d3 = s.getDistance();
                    break;
            }
        }
        // Se le envian las distancias al metodo que retorna la posición
        position = locationService.getLocation(d1, d2, d3);
        // Se declara una lista con los arrays que trae los fragmentos del
        // mensaje de cada satelite
        List<List<String>> msgSend = new ArrayList<>();
        // Se unifican los arrays de mensajes en una sola lista
        for (SatellitesDto s : satellites) {
            msgSend.add(s.getMessage());
        }
        // Se le envia esa lista al metodo que retorna en un String el mensaje
        // ya completo y descifrado
        String secretMessage = messageService.getMessage(msgSend);
        // Se valida que tanto la posición, como el mensaje se hayan podido determinar
        if (Objects.nonNull(position) && Objects.nonNull(secretMessage)) {
            responseMessage.setPosition(position);
            responseMessage.setMessage(secretMessage);
        }
        // Retorna la información procesada
        return responseMessage;
    }
}
