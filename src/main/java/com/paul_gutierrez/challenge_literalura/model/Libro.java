package com.paul_gutierrez.challenge_literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "libro_autores",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "libro_temas", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "tema")
    private List<String> temas;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "libro_idiomas", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "idioma")
    private List<String> idiomas;

    private Integer numeroDeDescargas;

    public Libro() {}

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.temas = datosLibro.temas();
        this.idiomas = datosLibro.idiomas();
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
        this.autores = new ArrayList<>();
    }


    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<String> getTemas() {
        return temas;
    }

    public void setTemas(List<String> temas) {
        this.temas = temas;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    @Override
    public String toString() {
        // Cadena con solo los nombres de los autores
        String autoresNombres = autores.stream()
                .map(Autor::getNombre)
                .collect(Collectors.joining(" | "));

        // Manejo de temas: Agrupar en líneas de tres temas
        String temasTexto;
        if (temas != null && !temas.isEmpty()) {
            StringBuilder temasBuilder = new StringBuilder();
            int temasPorLinea = 2;
            for (int i = 0; i < temas.size(); i++) {
                temasBuilder.append(temas.get(i));
                if ((i + 1) % temasPorLinea == 0 || i == temas.size() - 1) {
                    temasBuilder.append("\n       "); // Salto de línea con identación
                } else {
                    temasBuilder.append(" | ");
                }
            }
            temasTexto = temasBuilder.toString().trim();
        } else {
            temasTexto = "Sin temas";
        }

        return "----- Ficha Bibliográfica -----\n" +
                "Título: " + titulo + "\n" +
                "Autores: " + autoresNombres + "\n" +
                "Temas: " + temasTexto + "\n" +
                "Idiomas: " + String.join(", ", idiomas) + "\n" +
                "Número de descargas: " + numeroDeDescargas + "\n" +
                "-------------------------------";
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Integer numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }
}
