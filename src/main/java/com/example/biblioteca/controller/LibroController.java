package com.example.biblioteca.controller;

import com.example.biblioteca.model.Libro;
import com.example.biblioteca.service.LibroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libro")
public class LibroController {

    private final LibroService service;

    public LibroController(LibroService service) {
        this.service = service;
    }

    // ---------- CRUD ----------

    @GetMapping
    public ResponseEntity<List<Libro>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> traerUno(@PathVariable Long id) {
        return service.traerUno(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Libro> crear(@RequestBody Libro libro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(libro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> editar(@PathVariable Long id, @RequestBody Libro libro) {
        return service.editar(id, libro)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ---------- Situaciones especiales ----------

    @GetMapping("/disponibles")
    public ResponseEntity<List<Libro>> disponibles() {
        return ResponseEntity.ok(service.disponibles());
    }

    @GetMapping("/autor/{texto}")
    public ResponseEntity<List<Libro>> porAutor(@PathVariable String texto) {
        return ResponseEntity.ok(service.porAutor(texto));
    }

    @GetMapping("/antiguos")
    public ResponseEntity<List<Libro>> antiguos(@RequestParam(defaultValue = "1950") int antes) {
        return ResponseEntity.ok(service.antiguos(antes));
    }

    @PutMapping("/{id}/prestar")
    public ResponseEntity<Libro> prestar(@PathVariable Long id) {
        return responder(id, service.prestar(id));
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<Libro> devolver(@PathVariable Long id) {
        return responder(id, service.devolver(id));
    }

    // Convierte el resultado del servicio en el código de estado
    private ResponseEntity<Libro> responder(Long id, LibroService.Resultado resultado) {
        return switch (resultado) {
            case OK -> ResponseEntity.ok(service.traerUno(id).orElseThrow());
            case NO_EXISTE -> ResponseEntity.notFound().build();
            case CONFLICTO -> ResponseEntity.status(HttpStatus.CONFLICT).build();
        };
    }
}
