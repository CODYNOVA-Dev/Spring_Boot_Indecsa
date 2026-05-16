package com.example.demo.controller;

import com.example.demo.entity.AvancePartida;
import com.example.demo.service.AvancePartidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avances-partida")
@RequiredArgsConstructor
public class AvancePartidaController {

    private final AvancePartidaService service;

    @GetMapping
    public List<AvancePartida> findAll(@RequestParam(required = false) Integer idProyecto) {
        return idProyecto != null ? service.findByProyecto(idProyecto) : service.findAll();
    }

    @GetMapping("/{id}")
    public AvancePartida findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<AvancePartida> create(@RequestBody AvancePartida a) {
        return ResponseEntity.status(201).body(service.create(a));
    }

    @PutMapping("/{id}")
    public AvancePartida update(@PathVariable Integer id, @RequestBody AvancePartida a) { return service.update(id, a); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
