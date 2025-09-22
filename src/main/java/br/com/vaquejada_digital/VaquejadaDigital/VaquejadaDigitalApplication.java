package br.com.vaquejada_digital.VaquejadaDigital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true)
public class VaquejadaDigitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaquejadaDigitalApplication.class, args);
	}

}
