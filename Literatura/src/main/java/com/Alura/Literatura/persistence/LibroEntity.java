package com.Alura.Literatura.persistence;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "libros", uniqueConstraints = {
        @UniqueConstraint(name = "uk_libro_gutendex_id", columnNames = "gutendex_id")
})
public class LibroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gutendex_id", nullable = false)
    private Integer gutendexId; // id del libro en Gutendex

    @Column(nullable = false)
    private String titulo;

    @ElementCollection
    @CollectionTable(name = "libro_lenguajes", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "lenguaje")
    private List<String> lenguajes;

    @Column(name = "descargas")
    private Integer cantidadDescargas;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<AutorEntity> autores = new HashSet<>();

    // getters/setters

    public Long getId() { return id; }
    public Integer getGutendexId() { return gutendexId; }
    public void setGutendexId(Integer gutendexId) { this.gutendexId = gutendexId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public List<String> getLenguajes() { return lenguajes; }
    public void setLenguajes(List<String> lenguajes) { this.lenguajes = lenguajes; }
    public Integer getCantidadDescargas() { return cantidadDescargas; }
    public void setCantidadDescargas(Integer cantidadDescargas) { this.cantidadDescargas = cantidadDescargas; }
    public Set<AutorEntity> getAutores() { return autores; }
    public void setAutores(Set<AutorEntity> autores) { this.autores = autores; }
}
