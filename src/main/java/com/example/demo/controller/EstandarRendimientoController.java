package com.example.demo.controller;

import com.example.demo.entity.EstandarRendimiento;
import com.example.demo.service.EstandarRendimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estandares-rendimiento")
@RequiredArgsConstructor
public class EstandarRendimientoController {

    private final EstandarRendimientoService service;

    @GetMapping
    public List<EstandarRendimiento> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public EstandarRendimiento findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<EstandarRendimiento> create(@RequestBody EstandarRendimiento e) {
        return ResponseEntity.status(201).body(service.create(e));
    }

    @PutMapping("/{id}")
    public EstandarRendimiento update(@PathVariable Integer id, @RequestBody EstandarRendimiento e) { return service.update(id, e); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
