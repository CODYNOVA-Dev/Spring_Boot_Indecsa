package com.example.demo.controller;

import com.example.demo.dto.RendimientoIndicador;
import com.example.demo.service.RendimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rendimiento")
@RequiredArgsConstructor
public class RendimientoController {

    private final RendimientoService service;

    @GetMapping("/trabajador/{idTrabajador}")
    public List<RendimientoIndicador> porTrabajador(
            @PathVariable Integer idTrabajador,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return service.indicadoresPorTrabajador(idTrabajador, fechaInicio, fechaFin);
    }

    @GetMapping("/proyecto/{idProyecto}")
    public List<RendimientoIndicador> porProyecto(
            @PathVariable Integer idProyecto,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return service.indicadoresPorProyecto(idProyecto, fechaInicio, fechaFin);
    }
}
