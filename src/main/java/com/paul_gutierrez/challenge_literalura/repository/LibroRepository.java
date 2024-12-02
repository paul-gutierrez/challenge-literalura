package com.paul_gutierrez.challenge_literalura.repository;

import com.paul_gutierrez.challenge_literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LibroRepository extends JpaRepository<Libro, Long> {
    boolean existsByTitulo(String titulo);
}
