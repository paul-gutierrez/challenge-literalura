package com.paul_gutierrez.challenge_literalura.repository;

import com.paul_gutierrez.challenge_literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombre(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombre")
    Autor buscarPorNombre(@Param("nombre") String nombre);
}
