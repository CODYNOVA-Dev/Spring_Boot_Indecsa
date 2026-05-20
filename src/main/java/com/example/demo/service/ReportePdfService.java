package com.example.demo.service;

import com.example.demo.dto.RendimientoIndicador;
import com.example.demo.entity.AvancePartida;
import com.example.demo.entity.RegistroHoras;
import com.example.demo.entity.Trabajador;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AvancePartidaRepository;
import com.example.demo.repository.ProyectoRepository;
import com.example.demo.repository.RegistroHorasRepository;
import com.example.demo.repository.TrabajadorRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Genera reportes PDF tabulares con OpenPDF.
 * Todos los PDFs comparten el mismo estilo: cabecera con título y rango,
 * tabla principal, y pie con totales y fecha de generación.
 */
@Service
@RequiredArgsConstructor
public class ReportePdfService {

    private final RendimientoService rendimientoService;
    private final RegistroHorasRepository registroHorasRepo;
    private final AvancePartidaRepository avanceRepo;
    private final TrabajadorRepository    trabajadorRepo;
    private final ProyectoRepository      proyectoRepo;

    private static final Font FONT_TITULO   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(31, 41, 55));
    private static final Font FONT_SUBTIT   = FontFactory.getFont(FontFactory.HELVETICA, 11, new Color(75, 85, 99));
    private static final Font FONT_CABECERA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
    private static final Font FONT_CELDA    = FontFactory.getFont(FontFactory.HELVETICA, 9, new Color(17, 24, 39));
    private static final Font FONT_PIE      = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, new Color(107, 114, 128));

    private static final Color COLOR_CABECERA = new Color(13, 148, 136); // teal-600
    private static final Color COLOR_FILA_ALT = new Color(243, 244, 246);

    // ─── REPORTE 1: rendimiento por trabajador ────────────────────────────────

    public byte[] rendimientoTrabajador(Integer idTrabajador, LocalDate inicio, LocalDate fin) {
        Trabajador t = trabajadorRepo.findById(idTrabajador)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado: " + idTrabajador));
        List<RendimientoIndicador> indicadores =
                rendimientoService.indicadoresPorTrabajador(idTrabajador, inicio, fin);

        return generar(doc -> {
            agregarTitulo(doc, "Rendimiento de trabajador", t.getNombreTrabajador(), inicio, fin);

            PdfPTable tabla = nuevaTabla(new float[]{2.2f, 1.5f, 1f, 1.2f, 1.2f, 1.2f, 1.1f, 1f});
            for (String h : new String[]{
                    "Proyecto", "Cuadrilla", "Horas", "Avance", "Unidad",
                    "Rend. real", "Rend. esp.", "Desv."}) {
                tabla.addCell(celdaCabecera(h));
            }

            int idx = 0;
            for (RendimientoIndicador i : indicadores) {
                Color bg = (idx++ % 2 == 0) ? Color.WHITE : COLOR_FILA_ALT;
                tabla.addCell(celda(nz(i.getNombreProyecto()), bg));
                tabla.addCell(celda(nz(i.getNombreCuadrilla()), bg));
                tabla.addCell(celda(num(i.getTotalHorasTrabajadas(), 2), bg));
                tabla.addCell(celda(num(i.getTotalAvanceEjecutado(), 2), bg));
                tabla.addCell(celda(nz(i.getUnidadMedida()), bg));
                tabla.addCell(celda(num(i.getRendimientoReal(), 4), bg));
                tabla.addCell(celda(num(i.getRendimientoEsperado(), 4), bg));
                tabla.addCell(celda(pct(i.getPorcentajeDesviacion()), bg));
            }
            doc.add(tabla);

            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(
                    "Indicadores: VERDE ≥ -10%, AMARILLO -30%…-10%, ROJO < -30%, SIN_ESTANDAR si no hay referencia.",
                    FONT_PIE));
        });
    }

    // ─── REPORTE 2: horas por proyecto ────────────────────────────────────────

    public byte[] horasProyecto(Integer idProyecto, LocalDate inicio, LocalDate fin) {
        String nombreProyecto = proyectoRepo.findById(idProyecto)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado: " + idProyecto))
                .getNombreProyecto();
        List<RegistroHoras> registros = registroHorasRepo
                .findByProyectoAndFechaBetween(idProyecto, inicio, fin);

        return generar(doc -> {
            agregarTitulo(doc, "Horas trabajadas por proyecto", nombreProyecto, inicio, fin);

            PdfPTable tabla = nuevaTabla(new float[]{2.5f, 2f, 1.5f, 1.2f});
            for (String h : new String[]{"Trabajador", "Cuadrilla", "Fecha", "Horas"}) {
                tabla.addCell(celdaCabecera(h));
            }

            BigDecimal totalHoras = BigDecimal.ZERO;
            int idx = 0;
            for (RegistroHoras r : registros) {
                Color bg = (idx++ % 2 == 0) ? Color.WHITE : COLOR_FILA_ALT;
                String trabajador = r.getAsignacionTrabajadorProyecto() != null
                        && r.getAsignacionTrabajadorProyecto().getTrabajador() != null
                        ? r.getAsignacionTrabajadorProyecto().getTrabajador().getNombreTrabajador()
                        : "—";
                String cuadrilla = r.getCuadrilla() != null
                        ? r.getCuadrilla().getNombreCuadrilla() : "—";
                tabla.addCell(celda(nz(trabajador), bg));
                tabla.addCell(celda(nz(cuadrilla), bg));
                tabla.addCell(celda(r.getFechaRegistro() != null ? r.getFechaRegistro().toString() : "—", bg));
                tabla.addCell(celda(num(r.getHorasTrabajadas(), 2), bg));
                if (r.getHorasTrabajadas() != null) totalHoras = totalHoras.add(r.getHorasTrabajadas());
            }
            doc.add(tabla);

            doc.add(new Paragraph(" "));
            Paragraph total = new Paragraph(
                    "Total horas en el período: " + num(totalHoras, 2),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, new Color(13, 148, 136)));
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);
        });
    }

    // ─── REPORTE 3: avance por proyecto ───────────────────────────────────────

    public byte[] avanceProyecto(Integer idProyecto, LocalDate inicio, LocalDate fin) {
        String nombreProyecto = proyectoRepo.findById(idProyecto)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado: " + idProyecto))
                .getNombreProyecto();
        List<AvancePartida> avances = avanceRepo
                .findByProyectoAndFechaBetween(idProyecto, inicio, fin);

        return generar(doc -> {
            agregarTitulo(doc, "Avance de obra por proyecto", nombreProyecto, inicio, fin);

            PdfPTable tabla = nuevaTabla(new float[]{2.2f, 1.8f, 2f, 1.3f, 1.3f, 1f});
            for (String h : new String[]{"Partida", "Actividad estándar", "Cuadrilla", "Fecha", "Cantidad", "Unidad"}) {
                tabla.addCell(celdaCabecera(h));
            }

            BigDecimal totalAvance = BigDecimal.ZERO;
            int idx = 0;
            for (AvancePartida a : avances) {
                Color bg = (idx++ % 2 == 0) ? Color.WHITE : COLOR_FILA_ALT;
                String actividad = a.getEstandar() != null ? a.getEstandar().getNombreActividad() : "—";
                String unidad    = a.getEstandar() != null && a.getEstandar().getUnidadMedida() != null
                        ? a.getEstandar().getUnidadMedida().name() : "—";
                String cuadrilla = a.getCuadrilla() != null ? a.getCuadrilla().getNombreCuadrilla() : "—";

                tabla.addCell(celda(nz(a.getNombrePartida()), bg));
                tabla.addCell(celda(nz(actividad), bg));
                tabla.addCell(celda(nz(cuadrilla), bg));
                tabla.addCell(celda(a.getFechaRegistro() != null ? a.getFechaRegistro().toString() : "—", bg));
                tabla.addCell(celda(num(a.getCantidadEjecutada(), 2), bg));
                tabla.addCell(celda(unidad, bg));
                if (a.getCantidadEjecutada() != null) totalAvance = totalAvance.add(a.getCantidadEjecutada());
            }
            doc.add(tabla);

            doc.add(new Paragraph(" "));
            Paragraph total = new Paragraph(
                    "Total acumulado: " + num(totalAvance, 2),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, new Color(13, 148, 136)));
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);
        });
    }

    // ─── INFRA DE GENERACIÓN ──────────────────────────────────────────────────

    @FunctionalInterface
    private interface DocBuilder {
        void build(Document doc) throws Exception;
    }

    private byte[] generar(DocBuilder builder) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, 36, 36, 48, 48);
            PdfWriter.getInstance(doc, baos);
            doc.open();
            try {
                builder.build(doc);
            } finally {
                doc.close();
            }
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
        }
    }

    private void agregarTitulo(Document doc, String titulo, String subtitulo,
                               LocalDate inicio, LocalDate fin) throws Exception {
        Paragraph t = new Paragraph(titulo, FONT_TITULO);
        doc.add(t);

        if (subtitulo != null && !subtitulo.isEmpty()) {
            Paragraph s = new Paragraph(subtitulo, FONT_SUBTIT);
            doc.add(s);
        }
        Paragraph rango = new Paragraph(
                "Período: " + inicio + " → " + fin,
                FONT_SUBTIT);
        doc.add(rango);

        Paragraph gen = new Paragraph(
                "Generado: " + LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                FONT_PIE);
        doc.add(gen);
        doc.add(new Paragraph(" "));
    }

    private PdfPTable nuevaTabla(float[] anchos) {
        PdfPTable tabla = new PdfPTable(anchos.length);
        tabla.setWidthPercentage(100f);
        try {
            tabla.setWidths(anchos);
        } catch (Exception ignored) {}
        return tabla;
    }

    private PdfPCell celdaCabecera(String texto) {
        PdfPCell c = new PdfPCell(new Phrase(texto, FONT_CABECERA));
        c.setBackgroundColor(COLOR_CABECERA);
        c.setPadding(6f);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        return c;
    }

    private PdfPCell celda(String texto, Color bg) {
        PdfPCell c = new PdfPCell(new Phrase(texto != null ? texto : "—", FONT_CELDA));
        c.setBackgroundColor(bg);
        c.setPadding(5f);
        return c;
    }

    private static String nz(String s) { return s != null && !s.isEmpty() ? s : "—"; }

    private static String num(BigDecimal v, int scale) {
        if (v == null) return "—";
        return v.setScale(scale, RoundingMode.HALF_UP).toPlainString();
    }

    private static String pct(BigDecimal v) {
        if (v == null) return "—";
        return String.format(Locale.US, "%+.1f%%", v.doubleValue());
    }
}
