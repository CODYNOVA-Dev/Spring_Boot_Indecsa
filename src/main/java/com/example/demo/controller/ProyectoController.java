package com.example.demo.controller;

import com.example.demo.entity.Proyecto;
import com.example.demo.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService service;

    @GetMapping
    public List<Proyecto> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Proyecto findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Proyecto> create(@RequestBody Proyecto p) {
        return ResponseEntity.status(201).body(service.create(p));
    }

    @PutMapping("/{id}")
    public Proyecto update(@PathVariable Integer id, @RequestBody Proyecto p) { return service.update(id, p); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estatus")
    public Proyecto cambiarEstatus(@PathVariable Integer id, @RequestParam Proyecto.EstatusProyecto estatus) {
        Proyecto p = service.findById(id);
        p.setEstatusProyecto(estatus);
        return service.update(id, p);
    }
}
