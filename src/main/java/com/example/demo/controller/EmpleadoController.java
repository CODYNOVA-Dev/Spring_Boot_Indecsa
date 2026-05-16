package com.example.demo.controller;

import com.example.demo.entity.Empleado;
import com.example.demo.service.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService service;

    @GetMapping
    public List<Empleado> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Empleado findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Empleado> create(@RequestBody Empleado e) {
        return ResponseEntity.status(201).body(service.create(e));
    }

    @PutMapping("/{id}")
    public Empleado update(@PathVariable Integer id, @RequestBody Empleado e) { return service.update(id, e); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
