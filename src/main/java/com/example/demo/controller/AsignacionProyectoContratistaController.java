package com.example.demo.controller;

import com.example.demo.entity.AsignacionProyectoContratista;
import com.example.demo.service.AsignacionProyectoContratistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaciones-proyecto-contratista")
@RequiredArgsConstructor
public class AsignacionProyectoContratistaController {

    private final AsignacionProyectoContratistaService service;

    @GetMapping
    public List<AsignacionProyectoContratista> findAll(
            @RequestParam(required = false) Integer idProyecto,
            @RequestParam(required = false) Integer idContratista) {
        if (idProyecto != null) return service.findByProyecto(idProyecto);
        if (idContratista != null) return service.findByContratista(idContratista);
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AsignacionProyectoContratista findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<AsignacionProyectoContratista> create(@RequestBody AsignacionProyectoContratista a) {
        return ResponseEntity.status(201).body(service.create(a));
    }

    @PutMapping("/{id}")
    public AsignacionProyectoContratista update(@PathVariable Integer id, @RequestBody AsignacionProyectoContratista a) {
        return service.update(id, a);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
