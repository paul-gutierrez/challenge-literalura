package com.paul_gutierrez.challenge_literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutor> autores,
        @JsonAlias("subjects") List<String> temas,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Integer numeroDeDescargas) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(("-----------------------------\n"));
        sb.append("Título: ").append(titulo).append("\n");

        sb.append("Autores: ");
        if (autores != null && !autores.isEmpty()) {
            sb.append(autores.stream()
                    .map(DatosAutor::nombre)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Sin autores"));
        } else {
            sb.append("Sin autores");
        }
        sb.append("\n");

        sb.append("Temas: ");
        sb.append(temas != null && !temas.isEmpty() ? String.join(", ", temas) : "Sin temas");
        sb.append("\n");

        sb.append("Idiomas: ");
        sb.append(idiomas != null && !idiomas.isEmpty() ? String.join(", ", idiomas) : "Sin idiomas");
        sb.append("\n");

        sb.append("Número de descargas: ").append(numeroDeDescargas != null ? numeroDeDescargas : "Desconocido");
        sb.append(("\n-----------------------------"));
        return sb.toString();
    }
}
