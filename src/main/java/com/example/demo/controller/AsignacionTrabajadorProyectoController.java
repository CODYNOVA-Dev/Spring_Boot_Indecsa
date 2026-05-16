package com.example.demo.controller;

import com.example.demo.entity.AsignacionTrabajadorProyecto;
import com.example.demo.service.AsignacionTrabajadorProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaciones-trabajador-proyecto")
@RequiredArgsConstructor
public class AsignacionTrabajadorProyectoController {

    private final AsignacionTrabajadorProyectoService service;

    @GetMapping
    public List<AsignacionTrabajadorProyecto> findAll(
            @RequestParam(required = false) Integer idProyecto,
            @RequestParam(required = false) Integer idTrabajador) {
        if (idProyecto != null) return service.findByProyecto(idProyecto);
        if (idTrabajador != null) return service.findByTrabajador(idTrabajador);
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AsignacionTrabajadorProyecto findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<AsignacionTrabajadorProyecto> create(@RequestBody AsignacionTrabajadorProyecto a) {
        return ResponseEntity.status(201).body(service.create(a));
    }

    @PutMapping("/{id}")
    public AsignacionTrabajadorProyecto update(@PathVariable Integer id, @RequestBody AsignacionTrabajadorProyecto a) {
        return service.update(id, a);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
