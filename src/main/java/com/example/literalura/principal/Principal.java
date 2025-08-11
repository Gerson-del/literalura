package com.example.literalura.principal;

import com.example.literalura.model.*;
import com.example.literalura.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {
    private final Scanner scanner;
    private final ConsumoAPI consumoAPI;
    private final ConvierteDatos conversor;
    private final LibroService libroService;
    private final AutorService autorService;
    private final String URL = "https://gutendex.com/books/";

    @Autowired
    public Principal(ConsumoAPI consumoAPI, ConvierteDatos conversor,
                     LibroService libroService, AutorService autorService) {
        this.scanner = new Scanner(System.in);
        this.consumoAPI = consumoAPI;
        this.conversor = conversor;
        this.libroService = libroService;
        this.autorService = autorService;
    }

    public void muestraElMenu() {
        int opcion = -1;

        while (opcion != 0) {
            mostrarMenu();
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosEnAnio();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saliendo de la aplicación...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debes ingresar un número.");
                scanner.nextLine(); // Limpiar buffer
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n--- MENÚ LITERALURA ---");
        System.out.println("1. Buscar libro por título");
        System.out.println("2. Listar libros registrados");
        System.out.println("3. Listar autores registrados");
        System.out.println("4. Listar autores vivos en un año");
        System.out.println("5. Listar libros por idioma");
        System.out.println("0. Salir");
        System.out.print("Elija una opción: ");
    }

    private void buscarLibroPorTitulo() {
        System.out.print("\nIngrese el título del libro a buscar: ");
        String titulo = scanner.nextLine();

        try {
            // Buscar en la API
            String url = URL + "?search=" + titulo.replace(" ", "%20");
            String json = consumoAPI.obtenerDatos(url);
            Datos datos = conversor.obtenerDatos(json, Datos.class);

            if (datos.resultado().isEmpty()) {
                System.out.println("No se encontraron libros con ese título.");
                return;
            }

            DatosLibro datosLibro = datos.resultado().get(0);
            mostrarDatosLibro(datosLibro);
            guardarLibroYAutor(datosLibro);
        } catch (Exception e) {
            System.out.println("Error al buscar el libro: " + e.getMessage());
        }
    }

    private void mostrarDatosLibro(DatosLibro datosLibro) {
        System.out.println("\n--- LIBRO ENCONTRADO ---");
        System.out.println("Título: " + datosLibro.titulo());
        System.out.println("Autor: " + datosLibro.autor().get(0).nombre());
        System.out.println("Idiomas: " + String.join(", ", datosLibro.idiomas()));
        System.out.println("Número de descargas: " + datosLibro.numeroDeDescargas());
    }

    private void guardarLibroYAutor(DatosLibro datosLibro) {
        // Verificar si el libro ya existe
        if (libroService.buscarPorTitulo(datosLibro.titulo()).isPresent()) {
            System.out.println("\nEste libro ya está registrado en la base de datos.");
            return;
        }

        DatosAutor datosAutor = datosLibro.autor().get(0);
        Autor autor = new Autor();
        autor.setNombre(datosAutor.nombre());
        autor.setFechaNacimiento(datosAutor.fechaNacimiento());
        autor.setFechaMuerte(datosAutor.fechaMuerte());

        Libro libro = new Libro();
        libro.setTitulo(datosLibro.titulo());
        libro.setIdiomas(datosLibro.idiomas());
        libro.setNumeroDeDescargas(datosLibro.numeroDeDescargas());
        libro.setAutor(autor);

        autorService.guardarAutor(autor);
        libroService.guardarLibro(libro);
        System.out.println("\nLibro y autor guardados exitosamente en la base de datos!");
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroService.listarTodosLosLibros();

        if (libros.isEmpty()) {
            System.out.println("\nNo hay libros registrados en la base de datos.");
            return;
        }

        System.out.println("\n--- LIBROS REGISTRADOS ---");
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorService.listarTodosLosAutores();

        if (autores.isEmpty()) {
            System.out.println("\nNo hay autores registrados en la base de datos.");
            return;
        }

        System.out.println("\n--- AUTORES REGISTRADOS ---");
        autores.forEach(autor -> {
            System.out.println(autor);
            System.out.println("Libros escritos: " + autor.getLibros().size() + "\n");
        });
    }

    private void listarAutoresVivosEnAnio() {
        System.out.print("\nIngrese el año para buscar autores vivos: ");
        try {
            int anio = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            List<Autor> autores = autorService.listarAutoresVivosEnAnio(anio);

            if (autores.isEmpty()) {
                System.out.println("\nNo se encontraron autores vivos en el año " + anio);
                return;
            }

            System.out.println("\n--- AUTORES VIVOS EN " + anio + " ---");
            autores.forEach(System.out::println);
        } catch (InputMismatchException e) {
            System.out.println("\nError: Debes ingresar un año válido (número).");
            scanner.nextLine(); // Limpiar buffer
        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.print("\nIngrese el idioma para filtrar libros: ");
        String idioma = scanner.nextLine();

        List<Libro> libros = libroService.buscarPorIdioma(idioma.toLowerCase());

        if (libros.isEmpty()) {
            System.out.println("\nNo se encontraron libros en el idioma '" + idioma + "'");
            return;
        }

        System.out.println("\n--- LIBROS EN " + idioma.toUpperCase() + " ---");
        libros.forEach(libro -> {
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor().getNombre() + "\n");
        });
    }
}