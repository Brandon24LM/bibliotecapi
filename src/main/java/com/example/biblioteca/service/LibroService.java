package com.example.biblioteca.service;

import com.example.biblioteca.model.Libro;
import com.example.biblioteca.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    // Resultado de prestar / devolver
    public enum Resultado { OK, NO_EXISTE, CONFLICTO }

    private final LibroRepository repository;

    public LibroService(LibroRepository repository) {
        this.repository = repository;
    }

    // ---------- CRUD ----------

    public List<Libro> listarTodos() {
        return repository.findAll();
    }

    public Optional<Libro> traerUno(Long id) {
        return repository.findById(id);
    }

    public Libro crear(Libro libro) {
        libro.setId(null);
        return repository.save(libro);
    }

    public Optional<Libro> editar(Long id, Libro datos) {
        return repository.findById(id).map(libro -> {
            libro.setTitulo(datos.getTitulo());
            libro.setAutor(datos.getAutor());
            libro.setAnio(datos.getAnio());
            libro.setDisponible(datos.isDisponible());
            return repository.save(libro);
        });
    }

    public boolean eliminar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    // ---------- Situaciones especiales ----------

    // Filtro: solo los disponibles
    public List<Libro> disponibles() {
        return repository.findAll().stream()
                .filter(Libro::isDisponible)
                .toList();
    }

    // Consulta por nombre de método
    public List<Libro> porAutor(String texto) {
        return repository.findByAutorContainingIgnoreCase(texto);
    }

    // Parámetro de consulta: publicados antes de un año
    public List<Libro> antiguos(int antes) {
        return repository.findAll().stream()
                .filter(l -> l.getAnio() < antes)
                .toList();
    }

    // Cambio de estado: prestar (409 si ya estaba prestado)
    public Resultado prestar(Long id) {
        Optional<Libro> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            return Resultado.NO_EXISTE;
        }
        Libro libro = encontrado.get();
        if (!libro.isDisponible()) {
            return Resultado.CONFLICTO;
        }
        libro.setDisponible(false);
        repository.save(libro);
        return Resultado.OK;
    }

    // Cambio de estado: devolver (409 si no estaba prestado)
    public Resultado devolver(Long id) {
        Optional<Libro> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            return Resultado.NO_EXISTE;
        }
        Libro libro = encontrado.get();
        if (libro.isDisponible()) {
            return Resultado.CONFLICTO;
        }
        libro.setDisponible(true);
        repository.save(libro);
        return Resultado.OK;
    }
}
