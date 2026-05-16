package com.example.demo.controller;

import com.example.demo.entity.Trabajador;
import com.example.demo.service.TrabajadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trabajadores")
@RequiredArgsConstructor
public class TrabajadorController {

    private final TrabajadorService service;

    @GetMapping
    public List<Trabajador> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Trabajador findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Trabajador> create(@RequestBody Trabajador t) {
        return ResponseEntity.status(201).body(service.create(t));
    }

    @PutMapping("/{id}")
    public Trabajador update(@PathVariable Integer id, @RequestBody Trabajador t) { return service.update(id, t); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
