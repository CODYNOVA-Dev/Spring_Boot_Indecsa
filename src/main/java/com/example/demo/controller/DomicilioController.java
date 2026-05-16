package com.example.demo.controller;

import com.example.demo.entity.Domicilio;
import com.example.demo.service.DomicilioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/domicilios")
@RequiredArgsConstructor
public class DomicilioController {

    private final DomicilioService service;

    @GetMapping
    public List<Domicilio> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Domicilio findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Domicilio> create(@RequestBody Domicilio d) {
        return ResponseEntity.status(201).body(service.create(d));
    }

    @PutMapping("/{id}")
    public Domicilio update(@PathVariable Integer id, @RequestBody Domicilio d) { return service.update(id, d); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
