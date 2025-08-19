package com.Alura.Literatura.principal;

import com.Alura.Literatura.model.DatosAutor;
import com.Alura.Literatura.model.DatosBusqueda;
import com.Alura.Literatura.model.DatosLibro;
import com.Alura.Literatura.persistence.RegistroService;
import com.Alura.Literatura.service.ConsumoApi;
import com.Alura.Literatura.service.ConvierteDatos;
import com.Alura.Literatura.service.GutendexService;
import com.Alura.Literatura.persistence.LibroEntity;
import com.Alura.Literatura.persistence.AutorEntity;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner teclado = new Scanner(System.in);

    // Servicios "estilo curso" (no beans)
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final GutendexService servicio = new GutendexService(consumo, conversor);

    // Servicio de persistencia (bean de Spring) recibido por constructor
    private final RegistroService registro;

    public Principal(RegistroService registro) {
        this.registro = registro;
    }

    public void muestraElMenu() {
        int opcion;
        do {
            System.out.println("\n\t Menu Literatura \t\n ");
            System.out.println("1. Buscar libro por título");
            System.out.println("2. Listar libros (página inicial de Gutendex)");
            System.out.println("3. Listar autores de los resultados actuales");
            System.out.println("4. Listar autores vivos en un año");
            System.out.println("5. Listar libros por idioma");
            System.out.println("6. Guardar libro por ID en la base de datos");
            System.out.println("7. Listar libros guardados en la base de datos");
            System.out.println("0. Salir");
            System.out.print("Ingrese opción: ");

            while (!teclado.hasNextInt()) {
                System.out.print("Ingrese un número válido: ");
                teclado.next();
            }
            opcion = teclado.nextInt();
            teclado.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1 -> buscarPorTitulo();
                case 2 -> listarPaginaInicial();
                case 3 -> listarAutores();
                case 4 -> listarAutoresVivosEnAnio();
                case 5 -> listarPorIdioma();
                case 6 -> guardarPorId();
                case 7 -> listarGuardados();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida...");
            }
        } while (opcion != 0);
    }

    // -------- Opciones --------

    private void buscarPorTitulo() {
        System.out.print("Ingrese el título a buscar: ");
        String titulo = teclado.nextLine().trim();
        DatosBusqueda busqueda = servicio.buscarLibrosPorTitulo(titulo);
        imprimirLibros(busqueda);
    }

    private void listarPaginaInicial() {
        DatosBusqueda busqueda = servicio.listarPaginaInicial();
        imprimirLibros(busqueda);
    }

    private void listarAutores() {
        DatosBusqueda busqueda = servicio.listarPaginaInicial();
        imprimirAutores(busqueda);
    }

    private void listarAutoresVivosEnAnio() {
        System.out.print("Ingrese el año: ");
        Integer year = leerEnteroSeguro();
        if (year == null) return;

        DatosBusqueda busqueda = servicio.listarPaginaInicial();
        imprimirAutoresVivosEn(busqueda, year);
    }

    private void listarPorIdioma() {
        System.out.println("""
        Ingrese el idioma (código ISO):
         es = Español
         en = Inglés
         pt = Portugués
         fr = Francés
        """);
        String idioma = teclado.nextLine().trim().toLowerCase();
        DatosBusqueda busqueda = servicio.buscarLibrosPorIdioma(idioma);
        if (busqueda == null || busqueda.results() == null || busqueda.results().isEmpty()) {
            System.out.println("No se encontraron libros para el idioma: " + idioma);
        } else {
            busqueda.results().forEach(libro ->
                    System.out.println(" - " + safe(libro.titulo()) + "\n   Idiomas: " + safeList(libro.lenguajes())));
        }
    }

    private void guardarPorId() {
        if (registro == null) {
            System.out.println("Persistencia no disponible.");
            return;
        }
        System.out.print("Ingrese el ID de Gutendex (ej. 1342): ");
        Integer id = leerEnteroSeguro();
        if (id == null) return;

        DatosLibro libro = servicio.buscarLibroPorId(id);
        if (libro == null) {
            System.out.println("No se encontró el libro con id " + id);
            return;
        }

        boolean guardado = registro.guardarLibroSiNoExiste(libro);
        if (guardado) {
            System.out.println("✅ Libro guardado: " + safe(libro.titulo()) + " (ID Gutendex " + id + ")");
        } else {
            System.out.println("ℹ️ El libro ya estaba guardado (no se duplicó).");
        }
    }

    // -------- Helpers de impresión/null-safety --------

    private void imprimirLibros(DatosBusqueda busqueda) {
        if (busqueda == null || busqueda.results() == null || busqueda.results().isEmpty()) {
            System.out.println("Sin resultados.");
            return;
        }
        for (DatosLibro libro : busqueda.results()) {
            String autores = (libro.autores() == null || libro.autores().isEmpty())
                    ? "N/D"
                    : libro.autores().stream().map(DatosAutor::name).collect(Collectors.joining(", "));
            System.out.printf(" - %s (ID: %s) | Autores: %s | Idiomas: %s | Descargas: %s%n",
                    safe(libro.titulo()),
                    safe(libro.id()),
                    autores,
                    safeList(libro.lenguajes()),
                    safe(libro.cantidadDescargas()));
        }
    }

    private void imprimirAutores(DatosBusqueda busqueda) {
        if (busqueda == null || busqueda.results() == null || busqueda.results().isEmpty()) {
            System.out.println("Sin resultados.");
            return;
        }
        busqueda.results().forEach(libro -> {
            List<DatosAutor> autores = libro.autores();
            if (autores != null) {
                autores.forEach(autor ->
                        System.out.printf(" - %s (%s - %s)%n",
                                safe(autor.name()),
                                safe(autor.nacimiento()),
                                safe(autor.muerte())));
            }
        });
    }

    private void imprimirAutoresVivosEn(DatosBusqueda busqueda, int year) {
        if (busqueda == null || busqueda.results() == null || busqueda.results().isEmpty()) {
            System.out.println("Sin resultados.");
            return;
        }
        busqueda.results().forEach(libro -> {
            List<DatosAutor> autores = libro.autores();
            if (autores != null) {
                autores.stream()
                        .filter(a -> (a.nacimiento() != null && a.nacimiento() <= year) &&
                                (a.muerte() == null || a.muerte() >= year))
                        .forEach(a ->
                                System.out.println(" - " + safe(a.name()) + " estaba vivo en " + year));
            }
        });
    }

    private Integer leerEnteroSeguro() {
        if (!teclado.hasNextInt()) {
            System.out.println("Entrada inválida.");
            teclado.nextLine();
            return null;
        }
        int val = teclado.nextInt();
        teclado.nextLine();
        return val;
    }

    private static String safe(Object o) {
        return o == null ? "N/D" : String.valueOf(o);
    }

    private static String safeList(List<?> lst) {
        return (lst == null || lst.isEmpty()) ? "N/D" : lst.toString();
    }

    private void listarGuardados() {
        if (registro == null) {
            System.out.println("Persistencia no disponible.");
            return;
        }
        var lista = registro.listarLibros();
        if (lista == null || lista.isEmpty()) {
            System.out.println("Aún no hay libros guardados.");
            return;
        }

        for (LibroEntity l : lista) {
            String autores = (l.getAutores() == null || l.getAutores().isEmpty())
                    ? "N/D"
                    : l.getAutores().stream().map(AutorEntity::getNombre)
                    .collect(java.util.stream.Collectors.joining(", "));

            String lenguajes = (l.getLenguajes() == null || l.getLenguajes().isEmpty())
                    ? "N/D"
                    : l.getLenguajes().toString();

            System.out.printf("DB-ID: %s | Gutendex-ID: %s | Título: %s | Autores: %s | Idiomas: %s | Descargas: %s%n",
                    l.getId(), l.getGutendexId(), safe(l.getTitulo()), autores, lenguajes, safe(l.getCantidadDescargas()));
        }
    }
}

