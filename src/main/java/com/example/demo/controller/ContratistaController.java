package com.example.demo.controller;

import com.example.demo.entity.Contratista;
import com.example.demo.service.ContratistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratistas")
@RequiredArgsConstructor
public class ContratistaController {

    private final ContratistaService service;

    @GetMapping
    public List<Contratista> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Contratista findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Contratista> create(@RequestBody Contratista c) {
        return ResponseEntity.status(201).body(service.create(c));
    }

    @PutMapping("/{id}")
    public Contratista update(@PathVariable Integer id, @RequestBody Contratista c) { return service.update(id, c); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
