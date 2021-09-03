package com.meli.msfuegoquasar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;

/***
 * Clase main del microservicio
 * @author Fabrica Digital Microservicios
 * @versión 0.0.1-SNAPSHOT
 *
 */
@ComponentScan({"com.meli.*"})
@RefreshScope
@EnableCircuitBreaker
@EnableHystrixDashboard
@SpringBootApplication
public class MSFuegoQuasarApplication {

	/**
	 * Recurso que realiza la consulta de los seis últimos pagos realizados de
	 * una línea movil pospago de cliente Claro.
	 *
	 * Clase principal
	 *
	 * @author Fabrica Digital Microservicios
	 * @author Andrés Sierra
	 *
	 **/

	public static void main(String[] args) {
		SpringApplication.run(MSFuegoQuasarApplication.class, args);
	}

}
