package com.paul_gutierrez.challenge_literalura;

import com.paul_gutierrez.challenge_literalura.principal.Principal;
import com.paul_gutierrez.challenge_literalura.repository.AutorRepository;
import com.paul_gutierrez.challenge_literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLiteraluraApplication implements CommandLineRunner {

	@Autowired
	private LibroRepository libroRepository;

	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) { SpringApplication.run(ChallengeLiteraluraApplication.class, args); }

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(libroRepository, autorRepository);
		principal.muestraElMenu();
	}
}
