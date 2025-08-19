package com.Alura.Literatura;

import com.Alura.Literatura.persistence.RegistroService;
import com.Alura.Literatura.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {

	private final RegistroService registro;

	public LiteraturaApplication(RegistroService registro) {
		this.registro = registro;
	}

	public static void main(String[] args) {
		SpringApplication.run(LiteraturaApplication.class, args);
	}

	@Override
	public void run(String... args) {
		new Principal(registro).muestraElMenu(); // 👈 pasamos el servicio al menú
	}
}
