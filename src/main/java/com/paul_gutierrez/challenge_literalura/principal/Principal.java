package com.paul_gutierrez.challenge_literalura.principal;

import com.paul_gutierrez.challenge_literalura.model.Autor;
import com.paul_gutierrez.challenge_literalura.model.DatosAutor;
import com.paul_gutierrez.challenge_literalura.model.DatosLibro;
import com.paul_gutierrez.challenge_literalura.model.Libro;
import com.paul_gutierrez.challenge_literalura.repository.AutorRepository;
import com.paul_gutierrez.challenge_literalura.repository.LibroRepository;
import com.paul_gutierrez.challenge_literalura.service.ConsumoAPI;
import com.paul_gutierrez.challenge_literalura.service.ConvierteDatos;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                ----------------------------------------
                BIENVENIDO AL GESTOR DE LIBROS Y AUTORES
                ----------------------------------------
                Por favor, selecciona una opción:
                1 - Buscar libro por título
                2 - Mostrar libros registrados
                3 - Mostrar autores registrados
                4 - Mostrar autores vivos en determinado año
                5 - Mostrar libros por idioma
                0 - Salir
                """;
            System.out.print(menu);
            System.out.print("->");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibrosRegistrados();
                    break;
                case 3:
                    mostrarAutoresRegistrados();
                    break;
                case 4:
                    mostrarAutoresVivosEnFecha();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private DatosLibro getDatosLibro() {
        System.out.println("Escribe el título del libro que deseas buscar:");
        var terminoBusqueda = scanner.nextLine();

        // Construir la URL para la consulta
        String urlConsulta = URL_BASE + "?search=" + terminoBusqueda.replace(" ", "%20");

        var json = consumoAPI.obtenerDatos(urlConsulta);

        try {
            // Convertir la respuesta JSON en un objeto JSONObject
            JSONObject jsonObject = new JSONObject(json);

            // Validar si hay resultados en "results"
            if (jsonObject.has("results") && jsonObject.getJSONArray("results").length() > 0) {
                // Obtener el primer resultado
                JSONObject primerResultado = jsonObject.getJSONArray("results").getJSONObject(0);
                String primerResultadoString = primerResultado.toString();

                // Convertir a DatosLibro
                DatosLibro datosLibro = conversor.obtenerDatos(primerResultadoString, DatosLibro.class);

                // Mostrar el título del libro encontrado
                System.out.println("Resultado de la búsqueda: \n" + datosLibro);
                System.out.println("¿Es este el libro que buscabas? (sí/no)");

                // Confirmar la elección del usuario
                String eleccion = scanner.nextLine().trim().toLowerCase();
                if (eleccion.equals("sí") || eleccion.equals("si")) {
                    System.out.println(datosLibro);
                    return datosLibro;
                } else {
                    System.out.println("Intenta otra búsqueda.");
                    return null;
                }
            } else {
                System.out.println("Libro no encontrado.");
                return null;
            }
        } catch (JSONException e) {
            System.out.println("Ocurrió un error al procesar el JSON: " + e.getMessage());
            return null;
        }
    }

    private void buscarLibroPorTitulo() {
        DatosLibro datosLibro = getDatosLibro();

        if (datosLibro == null) {
            System.out.println("No se guardó ningún libro porque no se encontró o no fue confirmado.");
            return;
        }

        if (libroRepository.existsByTitulo(datosLibro.titulo())) {
            System.out.println("El libro ya está registrado en la base de datos: " + datosLibro.titulo());
            return;
        }

        // Crear y guardar el libro
        Libro libro = new Libro(datosLibro);
        libro.setAutores(new ArrayList<>()); // Inicia vacío para gestionar autores manualmente
        libro = libroRepository.save(libro);

        for (DatosAutor datosAutor : datosLibro.autores()) {
            Autor autor = autorRepository.buscarPorNombre(datosAutor.nombre());
            if (autor == null) {
                // Crear y guardar un nuevo autor
                autor = new Autor(
                        datosAutor.nombre(),
                        datosAutor.anioNacimiento(),
                        datosAutor.anioFallecimiento()
                );
                autor = autorRepository.save(autor);
            }

            // Insertar la relación manualmente
            libroRepository.insertarRelacionLibroAutor(libro.getId(), autor.getId());
        }

        System.out.println("Libro guardado exitosamente: " + libro.getTitulo());
    }

    private void mostrarLibrosRegistrados() {
        List<Libro> listaLibros = libroRepository.findAll();

        // Verificar si hay libros en la lista
        if (listaLibros.isEmpty()) {
            System.out.println("No se han encontrado libros registrados.");
        } else {
            System.out.println("Libros registrados:");
            listaLibros.forEach(libro -> {
                System.out.println(libro);
                System.out.println();
            });
        }
    }

    private void mostrarAutoresRegistrados() {
        List<Autor> listaAutores = autorRepository.findAll();

        // Verificar si hay autores en la lista
        if (listaAutores.isEmpty()) {
            System.out.println("No se han encontrado autores registrados.");
        } else {
            System.out.println("Autores registrados:");
            listaAutores.forEach(autor -> {
                System.out.println(autor);
                System.out.println();
            });
        }
    }

    private void mostrarAutoresVivosEnFecha() {
        System.out.println("Ingrese un año (por ejemplo, 1800): ");

        int anio;
        try {
            anio = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Año inválido. Por favor, ingrese un número válido.");
            return;
        }

        // Obtener la lista de autores vivos en el año proporcionado
        List<Autor> autoresVivos = autorRepository.findAll().stream()
                .filter(autor -> {
                    Integer nacimiento = autor.getAnioNacimiento();
                    Integer fallecimiento = autor.getAnioFallecimiento();
                    return (nacimiento != null && nacimiento <= anio) &&
                            (fallecimiento == null || fallecimiento >= anio);
                })
                .collect(Collectors.toList());

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año: " + anio);
        } else {
            System.out.println("Autores vivos en el año " + anio + ":");
            autoresVivos.forEach(autor -> {
                System.out.println(autor);
                System.out.println();
            });
        }
    }

    private Set<String> obtenerIdiomasDisponibles() {
        // Obtener todos los libros desde la base de datos
        List<Libro> listaLibros = libroRepository.findAll();

        // Usar streams para obtener idiomas únicos
        return listaLibros.stream()
                .flatMap(libro -> libro.getIdiomas().stream())
                .collect(Collectors.toSet());
    }

    private void mostrarLibrosPorIdioma() {
        // Obtener los idiomas disponibles
        Set<String> idiomasDisponibles = obtenerIdiomasDisponibles();

        if (idiomasDisponibles.isEmpty()) {
            System.out.println("No hay idiomas disponibles porque no hay libros registrados.");
            return;
        }

        // Mostrar el menú de idiomas
        System.out.println("Idiomas disponibles:");
        List<String> listaIdiomas = new ArrayList<>(idiomasDisponibles);
        for (int i = 0; i < listaIdiomas.size(); i++) {
            System.out.println((i + 1) + ". " + listaIdiomas.get(i));
        }

        // Pedir al usuario que seleccione un idioma
        System.out.print("Seleccione un idioma (número): ");
        int opcion;
        try {
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Intente de nuevo.");
            scanner.nextLine(); // Consumir entrada incorrecta
            return;
        }

        // Validar la opción
        if (opcion < 1 || opcion > listaIdiomas.size()) {
            System.out.println("Opción inválida.");
            return;
        }

        // Obtener el idioma seleccionado
        String idiomaSeleccionado = listaIdiomas.get(opcion - 1);

        // Filtrar y mostrar los libros en ese idioma
        List<Libro> librosEnIdioma = libroRepository.findAll().stream()
                .filter(libro -> libro.getIdiomas().contains(idiomaSeleccionado))
                .collect(Collectors.toList());

        if (librosEnIdioma.isEmpty()) {
            System.out.println("No hay libros disponibles en el idioma seleccionado: " + idiomaSeleccionado);
        } else {
            System.out.println("Libros disponibles en el idioma \"" + idiomaSeleccionado + "\":");
            librosEnIdioma.forEach(libro -> {
                System.out.println(libro);
                System.out.println();
            });
        }
    }
}
