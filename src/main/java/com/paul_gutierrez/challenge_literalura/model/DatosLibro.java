package com.paul_gutierrez.challenge_literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutor> autores,
        @JsonAlias("subjects") List<String> temas,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Integer numeroDeDescargas) {

    public List<Autor> convertirAEntidadesAutor() {
        if (autores == null || autores.isEmpty()) {
            return new ArrayList<>();
        }

        return autores.stream()
                .map(datoAutor -> {
                    Autor autor = new Autor();
                    autor.setNombre(datoAutor.nombre());
                    autor.setAnioNacimiento(datoAutor.anioNacimiento());
                    autor.setAnioFallecimiento(datoAutor.anioFallecimiento());
                    return autor;
                })
                .collect(Collectors.toList());
    }
}
