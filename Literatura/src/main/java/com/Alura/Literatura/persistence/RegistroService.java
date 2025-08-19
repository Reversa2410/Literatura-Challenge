package com.Alura.Literatura.persistence;

import com.Alura.Literatura.model.DatosLibro;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistroService {

    private final LibroRepository libros;
    private final AutorRepository autores;

    public RegistroService(LibroRepository libros, AutorRepository autores) {
        this.libros = libros;
        this.autores = autores;
    }

    @Transactional
    public boolean guardarLibroSiNoExiste(DatosLibro dato) {
        if (dato == null || dato.id() == null) return false;
        if (libros.existsByGutendexId(dato.id())) {
            return false; // ya existe → no guardar
        }

        // Mapear libro
        LibroEntity le = new LibroEntity();
        le.setGutendexId(dato.id());
        le.setTitulo(dato.titulo() == null ? "Sin título" : dato.titulo());
        le.setLenguajes(dato.lenguajes());
        le.setCantidadDescargas(dato.cantidadDescargas());

        // Mapear autores
        if (dato.autores() != null) {
            dato.autores().forEach(a -> {
                AutorEntity ae = autores
                        .findByNombreAndNacimiento(a.name(), a.nacimiento())
                        .orElseGet(() -> {
                            AutorEntity nuevo = new AutorEntity();
                            nuevo.setNombre(a.name());
                            nuevo.setNacimiento(a.nacimiento());
                            nuevo.setMuerte(a.muerte());
                            return autores.save(nuevo);
                        });
                le.getAutores().add(ae);
            });
        }

        libros.save(le);
        return true;
    }


    @Transactional(readOnly = true)
    public java.util.List<LibroEntity> listarLibros() {
        var lista = libros.findAll(Sort.by(Sort.Direction.DESC, "id"));
        lista.forEach(l -> {
            if (l.getAutores() != null)   l.getAutores().size();
            if (l.getLenguajes() != null) l.getLenguajes().size();
        });
        return lista;
    }
}
