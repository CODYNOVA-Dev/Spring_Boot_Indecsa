package com.example.demo.controller;

import com.example.demo.entity.Cuadrilla;
import com.example.demo.service.CuadrillaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuadrillas")
@RequiredArgsConstructor
public class CuadrillaController {

    private final CuadrillaService service;

    @GetMapping
    public List<Cuadrilla> findAll(@RequestParam(required = false) Integer idProyecto) {
        return idProyecto != null ? service.findByProyecto(idProyecto) : service.findAll();
    }

    @GetMapping("/{id}")
    public Cuadrilla findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Cuadrilla> create(@RequestBody Cuadrilla c) {
        return ResponseEntity.status(201).body(service.create(c));
    }

    @PutMapping("/{id}")
    public Cuadrilla update(@PathVariable Integer id, @RequestBody Cuadrilla c) { return service.update(id, c); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
