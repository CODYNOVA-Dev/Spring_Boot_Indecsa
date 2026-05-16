package com.example.demo.controller;

import com.example.demo.entity.RegistroMigratorio;
import com.example.demo.service.RegistroMigratorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros-migratorios")
@RequiredArgsConstructor
public class RegistroMigratorioController {

    private final RegistroMigratorioService service;

    @GetMapping
    public List<RegistroMigratorio> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public RegistroMigratorio findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<RegistroMigratorio> create(@RequestBody RegistroMigratorio r) {
        return ResponseEntity.status(201).body(service.create(r));
    }

    @PutMapping("/{id}")
    public RegistroMigratorio update(@PathVariable Integer id, @RequestBody RegistroMigratorio r) { return service.update(id, r); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
