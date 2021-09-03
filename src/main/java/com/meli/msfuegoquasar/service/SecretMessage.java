package com.meli.msfuegoquasar.service;

import com.meli.msfuegoquasar.exceptions.BadRequestException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * Clase service que contiene el algoritmo para unificar
 * los fragmentos del mensaje enviados por los satelites
 * y devolver un solo mensaje completo
 * @author Alejandro Balaguera
 *
 */
@Service
@Slf4j
public class SecretMessage {

    /**
     * Metodo que recibe una lista de Strings formada por los arrays
     * de mensajes que envia cada satelite, ademas tiene hystrix para
     * que en caso de que se demore mucho o tenga algun problema durante
     * su funcionamiento, se detenga y no siga consumiendo maquina.
     *
     * @author Alejandro Balaguera
     * @param messages, de tipo lista String
     * @return {@link String mensajeSecreto}
     **/
    @HystrixCommand(fallbackMethod = "defaultGetMessage", groupKey = "MSFueqoQuasar",
            commandKey = "MSFueqoQuasar")
    public String getMessage(List<List<String>> messages) {
        //Se declara una variable que contendra el tamaño maximo del mensaje en caso de que
        //los mensajes que lleguen sean de diferentes tamaños
        Integer maxSize = 0;
        //Se recorren todas las listas de mensajes para validar si hay alguna con mas
        //mensajes que otra
        for (var i = 0; i < messages.size(); i++) {
            //Si la lista en la posicion i es mayor al valor en maxSize
            //se hace maxSize igual al tamaño de la lista en posicion i
            if (maxSize < messages.get(i).size()) {
                maxSize = messages.get(i).size();
            }
        }
        //Se define el arreglo de string que contendra el mensaje descifrado
        var msg = new String[maxSize];
        // Se recorre el request que se recibe del servicio
        for (var i = 0; i < messages.size(); i++) {
            //Se recorre los elementos del mensaje en la posicion i
            for (var j = 0; j < messages.get(i).size(); j++) {
                //Se valida que el arreglo en la posicion j no contenga valor
                //para no sobreescribir una posicion que ya tenga valor y ademas
                //se valida que el mensaje en la posicion j sea diferente a vacio
                if (msg[j] == null && !messages.get(i).get(j).isEmpty()) {
                    //Se agrega el texto del mensaje en la posicion j al arreglo
                    // en su parte j
                    msg[j] = messages.get(i).get(j);
                }
            }
        }
        //retornamos el arreglo como string
        var mensajeSecreto = new StringBuilder();
        for (var i = 0; i < msg.length; i++) {
            //Se remplazan los campos null por vacios
            if (msg[i] == null) {
                msg[i] = " ";
            }
            //Si aun no se llega a la ultima posicion del arreglo
            //se deja un espacio despues de cada palabra
            if (i < msg.length - 1) {
                mensajeSecreto.append(msg[i]).append(" ");
            } else {
                mensajeSecreto.append(msg[i]);
            }
        }
        return mensajeSecreto.toString();
    }

    /**
     * Método por defecto vía Hystrix
     **/
    public String defaultGetMessage(List<List<String>> messages, Throwable ex) {
        log.info("messages: {}", messages.size());
        if (ex instanceof HystrixTimeoutException) {
            log.info("Ejecutando Fallbackmethod defaultGetMessage");
            return null;
        }
        throw new BadRequestException(ex.getMessage());
    }
}
