package com.paul_gutierrez.challenge_literalura.repository;

import com.paul_gutierrez.challenge_literalura.model.Libro;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LibroRepository extends JpaRepository<Libro, Long> {
    boolean existsByTitulo(String titulo);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO libro_autores (libro_id, autor_id) VALUES (:libroId, :autorId)", nativeQuery = true)
    void insertarRelacionLibroAutor(@Param("libroId") Long libroId, @Param("autorId") Long autorId);
}
