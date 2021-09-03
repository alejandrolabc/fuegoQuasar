package com.meli.msfuegoquasar.service;

import com.meli.msfuegoquasar.dto.LocationDto;
import com.meli.msfuegoquasar.exceptions.BadRequestException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/***
 * Clase service que contiene el algoritmo de trilateracion
 * tomado de la siguiente pagina
 * https://es.coredump.biz/questions/30336278/multipoint-trilateration-algorithm-in-java#31071455
 * @author Alejandro Balaguera
 *
 */
@Service
@Slf4j
public class LocationTrilateration {

    // Se toman las ubicaciones de los satelites definidas en el archivo de propiedades
    // por si despues de desplegado el aplicativo estas cambian, poder modificar solo
    // las propiedades sin bajar el servicio
    @Value("${kenobi.longitude}")
    String kenobiLongitude;
    @Value("${kenobi.latitude}")
    String kenobiLatitude;
    @Value("${skywalker.longitude}")
    String skywalkerLongitude;
    @Value("${skywalker.latitude}")
    String skywalkerLatitude;
    @Value("${sato.longitude}")
    String satoLongitude;
    @Value("${sato.latitude}")
    String satoLatitude;


    /**
     * Metodo que recibe las 3 distancias dadas por los satelites y que
     * por medio de la ecuación de trilateración determina la posicion
     * exacta, ademas tiene hystrix para que en caso de que se demore
     * mucho o tenga algun problema durante su funcionamiento, se detenga
     * y no siga consumiendo maquina.
     *
     * @author Alejandro Balaguera
     * @param distance1, de tipo double
     * @param distance2, de tipo double
     * @param distance3, de tipo double
     * @return {@link LocationDto LocationDto}
     **/
    @HystrixCommand(fallbackMethod = "defaultGetLocation", groupKey = "MSFueqoQuasar",
            commandKey = "MSFueqoQuasar")
    public LocationDto getLocation(double distance1, double distance2, double distance3) {

        //DECLARAR VARIABLES

        var p1 = new double[2];
        var p2 = new double[2];
        var p3 = new double[2];
        var ex = new double[2];
        var ey = new double[2];
        var p3p1 = new double[2];
        var jval = 0.0;
        var temp = 0.0;
        var ival = 0.0;
        var p3p1i = 0.0;
        double triptx;
        double tripty;
        double xval;
        double yval;
        double t1;
        double t2;
        double t3;
        double t;
        double exx;
        double d;
        double eyy;

        //TRADUCIR PUNTOS DE SATELITES A VECTORES
        //POINT 1
        p1[0] = Double.valueOf(kenobiLongitude);
        p1[1] = Double.valueOf(kenobiLatitude);
        //POINT 2
        p2[0] = Double.valueOf(skywalkerLongitude);
        p2[1] = Double.valueOf(skywalkerLatitude);
        //POINT 3
        p3[0] = Double.valueOf(satoLongitude);
        p3[1] = Double.valueOf(satoLatitude);

        for (var i = 0; i < p1.length; i++) {
            t1 = p2[i];
            t2 = p1[i];
            t = t1 - t2;
            temp += (t * t);
        }
        d = Math.sqrt(temp);
        for (var i = 0; i < p1.length; i++) {
            t1 = p2[i];
            t2 = p1[i];
            exx = (t1 - t2) / (Math.sqrt(temp));
            ex[i] = exx;
        }
        for (var i = 0; i < p3.length; i++) {
            t1 = p3[i];
            t2 = p1[i];
            t3 = t1 - t2;
            p3p1[i] = t3;
        }
        for (var i = 0; i < ex.length; i++) {
            t1 = ex[i];
            t2 = p3p1[i];
            ival += (t1 * t2);
        }
        for (var i = 0; i < p3.length; i++) {
            t1 = p3[i];
            t2 = p1[i];
            t3 = ex[i] * ival;
            t = t1 - t2 - t3;
            p3p1i += (t * t);
        }
        for (var i = 0; i < p3.length; i++) {
            t1 = p3[i];
            t2 = p1[i];
            t3 = ex[i] * ival;
            eyy = (t1 - t2 - t3) / Math.sqrt(p3p1i);
            ey[i] = eyy;
        }
        for (var i = 0; i < ey.length; i++) {
            t1 = ey[i];
            t2 = p3p1[i];
            jval += (t1 * t2);
        }
        xval = (Math.pow(distance1, 2) - Math.pow(distance2, 2) + Math.pow(d, 2)) / (2 * d);
        yval = ((Math.pow(distance1, 2) - Math.pow(distance3, 2) + Math.pow(ival, 2) + Math.pow(jval, 2)) / (2 * jval)) - ((ival / jval) * xval);

        t1 = Double.valueOf(kenobiLongitude);
        t2 = ex[0] * xval;
        t3 = ey[0] * yval;
        triptx = t1 + t2 + t3;

        t1 = Double.valueOf(kenobiLatitude);
        t2 = ex[1] * xval;
        t3 = ey[1] * yval;
        tripty = t1 + t2 + t3;

        return new LocationDto(triptx, tripty);
    }

    /**
     * Método por defecto vía Hystrix
     **/
    public LocationDto defaultGetLocation(double distance1, double distance2, double distance3, Throwable ex) {
        log.info("distancias: {}, {}, {}", distance1, distance2, distance3);
        if (ex instanceof HystrixTimeoutException) {
            log.info("Ejecutando Fallbackmethod defaultGetLocation");
            return null;
        }
        throw new BadRequestException(ex.getMessage());
    }
}
