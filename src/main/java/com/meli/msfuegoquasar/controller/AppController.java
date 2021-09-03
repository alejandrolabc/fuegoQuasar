package com.meli.msfuegoquasar.controller;

import com.meli.msfuegoquasar.application.MessageIntersectionApplication;
import com.meli.msfuegoquasar.dto.RequestTemporalDto;
import com.meli.msfuegoquasar.dto.RequestDto;
import com.meli.msfuegoquasar.dto.ResponseDto;
import com.meli.msfuegoquasar.dto.SatellitesDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/***
 * Clase controlador del microservicio
 * @author Alejandro Balaguera
 *
 */
@RestController
@RequestMapping
public class AppController {

    @Autowired
    private MessageIntersectionApplication app;

    //Variable de clase para almacenar la información de los satelites que lleguen
    // por el path topsecret_split
    List<SatellitesDto> satellites;

    /**
     * Recurso que determina la posición y el mensaje de la nave imperial.
     * <p>
     * Esta clase implementa la ruta del microservicio así como el método por el cual lo expone
     * Además requiere parametros de consulta
     *
     * @author Alejandro Balaguera
     * @param request, de tipo RequestDto
     * @return {@link ResponseEntity <ResponseDto> response}
     **/
    @PostMapping("/topsecret/")
    @ApiOperation(value = "Recurso que retorna el mensaje secreto y su posicion.", notes = "", response = ResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = ResponseDto.class, message = "OK"),
            @ApiResponse(code = 400, response = ResponseDto.class, message = "Bad request"),
            @ApiResponse(code = 404, response = ResponseDto.class, message = "Not found"),
            @ApiResponse(code = 500, response = ResponseDto.class, message = "Internal server error"),
            @ApiResponse(code = 504, response = ResponseDto.class, message = "Gateway timeout")})
    public ResponseEntity<ResponseDto> getMessageIntersection(@RequestBody RequestDto request) {
        //Se llama el metodo de la capa de aplicacion para recibir la posicion y el mensaje
        ResponseDto response = app.getMessageAndPosition(request.getSatellites());
        //Se valida si fue posible determinar la posicion y el mensaje
        if (Objects.nonNull(response)) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Recurso via post que determina la posición y el mensaje de la nave imperial por medio
     * de varias peticiones, las cuales, va almacenando en memoria hasta completar la información
     * necesaria para poder determinar la posición y el mensaje.
     * <p>
     * Esta clase implementa la ruta del microservicio así como el método por el cual lo expone
     * Además requiere parametros de consulta
     *
     * @author Alejandro Balaguera
     * @param name, de tipo String y contiene el nombre del satellite
     * @param request, de tipo RequestTemporalDto que contiene la distancia y el mensaje del satelite en cuestion
     * @return {@link ResponseEntity <ResponseDto> response}
     **/
    @PostMapping("/topsecret_split/{satellite_name}")
    @ApiOperation(value = "Recurso que retorna el mensaje secreto y su posicion. pero por medio de diferentes" +
            "peticiones, almacenando en cache, los datos de los satelites", notes = "", response = ResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = ResponseDto.class, message = "OK"),
            @ApiResponse(code = 400, response = ResponseDto.class, message = "Bad request"),
            @ApiResponse(code = 404, response = ResponseDto.class, message = "Not found"),
            @ApiResponse(code = 500, response = ResponseDto.class, message = "Internal server error"),
            @ApiResponse(code = 504, response = ResponseDto.class, message = "Gateway timeout")})
    public ResponseEntity<ResponseDto> getMessageIntersectionByTemporal(@PathVariable("satellite_name") String name,
                                                                      @RequestBody RequestTemporalDto request) {
        ResponseDto response = null;
        // Se inicializa la lista temporal en caso de estar vacia
        if (satellites == null) satellites = new ArrayList<>();

        // Se valida si ya tiene la información de los 3 satelites
        if (satellites.size() >= 3) {
            response = app.getMessageAndPosition(satellites);
        } else {
            // Se valida si de la información que tiene, ya se tiene información
            // del satelite que llega
            Boolean existe = false;
            for (SatellitesDto s : satellites) {
                if (s.getName().equals(name)) {
                    existe = true;
                    break;
                }
            }
            // Si no se tiene información de ese satelite se agrega
            if (Boolean.FALSE.equals(existe)) {
                satellites.add(new SatellitesDto(name, request.getDistance(), request.getMessage()));
                //Se valida si ya tiene la información de los 3 satelites para determinar posicion y mensaje
                if (satellites.size() >= 3) response = app.getMessageAndPosition(satellites);
            }
        }
        if (Objects.nonNull(response)) {
            //Si se pudo determinar la posicion y el mensaje, se vacia la lista y se retorna la informacion con
            // un codigo 200 de que el aplicativo se ejecuto correctamente
            satellites = new ArrayList<>();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = new ResponseDto();
            response.setMessage("No fue posible determinar la posición o el mensaje, datos de satelites incompletos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Recurso via get que determina la posición y el mensaje de la nave imperial por medio
     * de varias peticiones, las cuales, va almacenando en memoria hasta completar la información
     * necesaria para poder determinar la posición y el mensaje.
     * <p>
     * Esta clase implementa la ruta del microservicio así como el método por el cual lo expone
     * Además requiere parametros de consulta
     *
     * @author Alejandro Balaguera
     * @param name, de tipo String y contiene el nombre del satellite
     * @param distance, de tipo Double que contiene la distancia del satelite en cuestion
     * @param message, de tipo String que contiene el mensaje del satelite en cuestion
     * @return {@link ResponseEntity <ResponseDto> response}
     **/
    @GetMapping("/topsecret_split/{satellite_name}")
    @ApiOperation(value = "Recurso que retorna el mensaje secreto y su posicion. pero por medio de diferentes" +
            "peticiones, almacenando en cache, los datos de los satelites", notes = "", response = ResponseDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = ResponseDto.class, message = "OK"),
            @ApiResponse(code = 400, response = ResponseDto.class, message = "Bad request"),
            @ApiResponse(code = 404, response = ResponseDto.class, message = "Not found"),
            @ApiResponse(code = 500, response = ResponseDto.class, message = "Internal server error"),
            @ApiResponse(code = 504, response = ResponseDto.class, message = "Gateway timeout")})
    public ResponseEntity<ResponseDto> getMessageIntersectionByTemporal(@PathVariable("satellite_name") String name,
                                                                        @RequestParam() Double distance,
                                                                        @RequestParam() String message) {
        ResponseDto response = null;
        // Se inicializa la lista temporal en caso de estar vacia
        if (satellites == null) satellites = new ArrayList<>();

        // Se valida si ya tiene la información de los 3 satelites
        if (satellites.size() >= 3) {
            response = app.getMessageAndPosition(satellites);
        } else {
            // Se valida si de la información que tiene, ya se tiene información
            // del satelite que llega
            Boolean existe = false;
            for (SatellitesDto s : satellites) {
                if (s.getName().equals(name)) {
                    existe = true;
                    break;
                }
            }
            // Si no se tiene información de ese satelite se agrega
            if (Boolean.FALSE.equals(existe)) {
                //Se hace un split del message para almacenarlo en una lista
                String[] split = message.split(",");
                List<String> messages = new ArrayList<>();
                for(String m: split){
                    if(m.equals(" ")){
                        messages.add("");
                    } else {
                        messages.add(m);
                    }
                }
                satellites.add(new SatellitesDto(name, distance, messages));
                //Se valida si ya tiene la información de los 3 satelites para determinar posicion y mensaje
                if (satellites.size() >= 3) response = app.getMessageAndPosition(satellites);
            }
        }
        if (Objects.nonNull(response)) {
            //Si se pudo determinar la posicion y el mensaje, se vacia la lista y se retorna la informacion con
            // un codigo 200 de que el aplicativo se ejecuto correctamente
            satellites = new ArrayList<>();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = new ResponseDto();
            response.setMessage("No fue posible determinar la posición o el mensaje, datos de satelites incompletos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


}
