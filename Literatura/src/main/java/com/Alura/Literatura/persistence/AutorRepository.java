package com.Alura.Literatura.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<AutorEntity, Long> {
    Optional<AutorEntity> findByNombreAndNacimiento(String nombre, Integer nacimiento);
}
