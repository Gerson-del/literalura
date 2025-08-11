package com.example.literalura.repository;

import com.example.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT a FROM Autor a WHERE (:anio >= a.fechaNacimiento) AND (a.fechaMuerte IS NULL OR :anio <= a.fechaMuerte)")
    List<Autor> findAutoresVivosEnAnio(@Param("anio") Integer anio);
}