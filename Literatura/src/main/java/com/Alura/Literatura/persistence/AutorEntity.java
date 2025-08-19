package com.Alura.Literatura.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "autores", uniqueConstraints = {
        @UniqueConstraint(name = "uk_autor_nombre_nacimiento", columnNames = {"nombre", "nacimiento"})
})
public class AutorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column
    private Integer nacimiento;

    @Column
    private Integer muerte;

    // getters/setters

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getNacimiento() { return nacimiento; }
    public void setNacimiento(Integer nacimiento) { this.nacimiento = nacimiento; }
    public Integer getMuerte() { return muerte; }
    public void setMuerte(Integer muerte) { this.muerte = muerte; }
}
