package com.paul_gutierrez.challenge_literalura.principal;

import com.paul_gutierrez.challenge_literalura.model.DatosLibro;
import com.paul_gutierrez.challenge_literalura.model.Libro;
import com.paul_gutierrez.challenge_literalura.repository.LibroRepository;
import com.paul_gutierrez.challenge_literalura.service.ConsumoAPI;
import com.paul_gutierrez.challenge_literalura.service.ConvierteDatos;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Scanner;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";
    private LibroRepository repository;

    public Principal(LibroRepository repository) {
        this.repository = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                1 - Buscar libro por título
                2 - Mostrar libros registrados
                3 - Mostrar autores resgtrados
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
                    // buscarLibroPorIdioma();
                    break;
                case 3:
                    // mostrarLibrosBuscados();
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
                System.out.println("Título encontrado: " + datosLibro.titulo());
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

        // Verificar si el libro ya existe en la base de datos
        if (repository.existsByTitulo(datosLibro.titulo())) {
            System.out.println("El libro ya está registrado en la base de datos: " + datosLibro.titulo());
            return;
        }

        // Intentar guardar el libro
        Libro libro = new Libro(datosLibro);
        try {
            repository.save(libro);
            System.out.println("Libro guardado exitosamente: " + libro.getTitulo());
        } catch (Exception e) {
            System.out.println("Ocurrió un error al guardar el libro: " + e.getMessage());
        }
    }
}
