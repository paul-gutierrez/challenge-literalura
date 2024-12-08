package com.paul_gutierrez.challenge_literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    private Integer anioNacimiento;
    private Integer anioFallecimiento;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    public Autor() {}

    public Autor(String nombre, Integer anioNacimiento, Integer anioFallecimiento) {
        this.nombre = nombre;
        this.anioNacimiento = anioNacimiento;
        this.anioFallecimiento = anioFallecimiento;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(Integer anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public Integer getAnioFallecimiento() {
        return anioFallecimiento;
    }

    public void setAnioFallecimiento(Integer anioFallecimiento) {
        this.anioFallecimiento = anioFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        // Convertir la lista de libros a una lista de títulos separados por comas
        String librosTexto = libros.isEmpty()
                ? "Ningún libro registrado"
                : libros.stream().map(Libro::getTitulo).collect(Collectors.joining(" | "));

        return "----- Ficha de Autor -----\n" +
                "Autor: " + nombre + "\n" +
                "Fecha de nacimiento: " + (anioNacimiento != null ? anioNacimiento : "Desconocida") + "\n" +
                "Fecha de fallecimiento: " + (anioFallecimiento != null ? anioFallecimiento : "Desconocida") + "\n" +
                "Libros: " + librosTexto + "\n" +
                "---------------------------";
    }
}
