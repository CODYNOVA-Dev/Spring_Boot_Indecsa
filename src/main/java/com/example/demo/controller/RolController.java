package com.example.demo.controller;

import com.example.demo.entity.Rol;
import com.example.demo.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService service;

    @GetMapping
    public List<Rol> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Rol findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Rol> create(@RequestBody Rol r) {
        return ResponseEntity.status(201).body(service.create(r));
    }

    @PutMapping("/{id}")
    public Rol update(@PathVariable Integer id, @RequestBody Rol r) { return service.update(id, r); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
