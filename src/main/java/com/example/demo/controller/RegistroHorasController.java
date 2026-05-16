package com.example.demo.controller;

import com.example.demo.entity.RegistroHoras;
import com.example.demo.service.RegistroHorasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros-horas")
@RequiredArgsConstructor
public class RegistroHorasController {

    private final RegistroHorasService service;

    @GetMapping
    public List<RegistroHoras> findAll(@RequestParam(required = false) Integer idAsignacionTp) {
        return idAsignacionTp != null ? service.findByAsignacionTp(idAsignacionTp) : service.findAll();
    }

    @GetMapping("/{id}")
    public RegistroHoras findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<RegistroHoras> create(@RequestBody RegistroHoras r) {
        return ResponseEntity.status(201).body(service.create(r));
    }

    @PutMapping("/{id}")
    public RegistroHoras update(@PathVariable Integer id, @RequestBody RegistroHoras r) { return service.update(id, r); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
