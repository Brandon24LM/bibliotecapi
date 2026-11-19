package com.example.biblioteca.repository;

import com.example.biblioteca.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Consulta por nombre de método: autor contiene el texto, sin importar mayúsculas
    List<Libro> findByAutorContainingIgnoreCase(String texto);
}
