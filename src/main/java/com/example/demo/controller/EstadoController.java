package com.example.demo.controller;

import com.example.demo.entity.Estado;
import com.example.demo.service.EstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoService service;

    @GetMapping
    public List<Estado> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Estado findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Estado> create(@RequestBody Estado e) {
        return ResponseEntity.status(201).body(service.create(e));
    }

    @PutMapping("/{id}")
    public Estado update(@PathVariable Integer id, @RequestBody Estado e) { return service.update(id, e); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
