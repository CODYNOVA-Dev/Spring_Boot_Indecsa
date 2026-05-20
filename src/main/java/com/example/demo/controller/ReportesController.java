package com.example.demo.controller;

import com.example.demo.service.ReportePdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Endpoints de generación de reportes en PDF.
 * Todos devuelven application/pdf con Content-Disposition attachment.
 */
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReportesController {

    private final ReportePdfService service;

    @GetMapping("/rendimiento/trabajador/{id}")
    public ResponseEntity<byte[]> rendimientoTrabajador(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        byte[] pdf = service.rendimientoTrabajador(id, fechaInicio, fechaFin);
        return responsePdf(pdf, "rendimiento_trabajador_" + id + "_" + fechaInicio + ".pdf");
    }

    @GetMapping("/horas/proyecto/{id}")
    public ResponseEntity<byte[]> horasProyecto(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        byte[] pdf = service.horasProyecto(id, fechaInicio, fechaFin);
        return responsePdf(pdf, "horas_proyecto_" + id + "_" + fechaInicio + ".pdf");
    }

    @GetMapping("/avance/proyecto/{id}")
    public ResponseEntity<byte[]> avanceProyecto(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        byte[] pdf = service.avanceProyecto(id, fechaInicio, fechaFin);
        return responsePdf(pdf, "avance_obra_" + id + "_" + fechaInicio + ".pdf");
    }

    private ResponseEntity<byte[]> responsePdf(byte[] pdf, String nombreArchivo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", nombreArchivo);
        headers.setContentLength(pdf.length);
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
