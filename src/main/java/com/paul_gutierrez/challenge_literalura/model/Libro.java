package com.paul_gutierrez.challenge_literalura.model;

import jakarta.persistence.*;

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


    @ElementCollection
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

        this.autores = datosLibro.autores().stream()
                .map(datosAutor -> {
                    Autor autor = new Autor(
                            datosAutor.nombre(),
                            datosAutor.anioNacimiento(),
                            datosAutor.anioFallecimiento()
                    );
                    autor.getLibros().add(this);
                    return autor;
                })
                .toList();

        this.temas = datosLibro.temas();
        this.idiomas = datosLibro.idiomas();
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
    }

    // Getters y Setters
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
                .collect(Collectors.joining(", "));

        return "----- Ficha Bibliográfica -----\n" +
                "Título: " + titulo + "\n" +
                "Autores: " + autoresNombres + "\n" +
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
