package com.Alura.Literatura.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<LibroEntity, Long> {
    Optional<LibroEntity> findByGutendexId(Integer gutendexId);
    boolean existsByGutendexId(Integer gutendexId);
}
