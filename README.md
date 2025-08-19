# Literatura-Challenge
Challenge de Alura, Literatura, backend y base de datos
Literatura (Consola + Gutendex + JPA)

Aplicación de consola en Java que consulta la API pública Gutendex para buscar libros y persistirlos en PostgreSQL usando Spring Data JPA. Sigue el estilo del curso (menú en consola vía CommandLineRunner, servicios simples y DTOs como records).

Tecnologías

Java 21

Spring Boot 3.5.4

Spring Data JPA

PostgreSQL

Jackson (JSON)

Java HttpClient (JDK 11+)

Maven

Funcionalidades

Buscar libros por título (Gutendex).

Listar la página inicial de Gutendex.

Listar autores y autores vivos en un año dado (desde los resultados).

Filtrar por idioma (códigos ISO: es, en, pt, fr, …).

Guardar un libro por ID de Gutendex en la base de datos (sin duplicar).

Listar libros guardados en la base de datos.
