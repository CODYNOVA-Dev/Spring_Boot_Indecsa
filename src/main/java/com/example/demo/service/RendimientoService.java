package com.example.demo.service;

import com.example.demo.dto.RendimientoIndicador;
import com.example.demo.entity.AvancePartida;
import com.example.demo.entity.Cuadrilla;
import com.example.demo.entity.EstandarRendimiento;
import com.example.demo.entity.Proyecto;
import com.example.demo.entity.RegistroHoras;
import com.example.demo.entity.Trabajador;
import com.example.demo.repository.AvancePartidaRepository;
import com.example.demo.repository.RegistroHorasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Calcula indicadores de rendimiento combinando RegistroHoras + AvancePartida.
 *
 * Granularidad del indicador: (trabajador, proyecto, cuadrilla).
 * Para atribuir avance a un trabajador dentro de su cuadrilla se reparte
 * proporcional a sus horas vs el total de horas de la cuadrilla en el período.
 *
 *     contribucionAvanceTrabajador =
 *         (horasTrabajador / horasCuadrilla) * avanceCuadrilla
 *
 * El rendimientoReal se calcula como contribucionAvance / horasTrabajador.
 * El esperado proviene del estandar del avance dominante (más cantidad) en
 * esa cuadrilla; si todos los avances son nulos de estandar → SIN_ESTANDAR.
 */
@Service
@RequiredArgsConstructor
public class RendimientoService {

    private final RegistroHorasRepository registroHorasRepo;
    private final AvancePartidaRepository avanceRepo;

    public List<RendimientoIndicador> indicadoresPorTrabajador(Integer idTrabajador,
                                                               LocalDate inicio,
                                                               LocalDate fin) {
        List<RegistroHoras> registros = registroHorasRepo
                .findByTrabajadorAndFechaBetween(idTrabajador, inicio, fin);

        // Agrupar registros por (idProyecto, idCuadrilla)
        Map<ClaveGrupo, List<RegistroHoras>> registrosPorGrupo = agruparRegistros(registros);

        List<RendimientoIndicador> resultados = new ArrayList<>();
        for (Map.Entry<ClaveGrupo, List<RegistroHoras>> entry : registrosPorGrupo.entrySet()) {
            ClaveGrupo clave = entry.getKey();
            List<RegistroHoras> regsTrabajador = entry.getValue();

            // Horas del trabajador en esa cuadrilla
            BigDecimal horasTrabajador = sumarHoras(regsTrabajador);

            // Horas totales de la cuadrilla (de todos los trabajadores) — para el reparto
            List<RegistroHoras> regsCuadrilla = registroHorasRepo
                    .findByProyectoAndFechaBetween(clave.idProyecto, inicio, fin)
                    .stream()
                    .filter(r -> Objects.equals(idCuadrilla(r), clave.idCuadrilla))
                    .toList();
            BigDecimal horasCuadrilla = sumarHoras(regsCuadrilla);

            // Avances de esa cuadrilla en el período
            List<AvancePartida> avances = avanceRepo
                    .findByProyectoAndFechaBetween(clave.idProyecto, inicio, fin)
                    .stream()
                    .filter(a -> Objects.equals(idCuadrillaDeAvance(a), clave.idCuadrilla))
                    .toList();
            BigDecimal avanceCuadrilla = sumarCantidades(avances);

            BigDecimal contribucionAvance;
            if (horasCuadrilla.signum() > 0) {
                contribucionAvance = horasTrabajador
                        .divide(horasCuadrilla, 6, RoundingMode.HALF_UP)
                        .multiply(avanceCuadrilla);
            } else {
                contribucionAvance = BigDecimal.ZERO;
            }

            BigDecimal rendimientoReal = horasTrabajador.signum() > 0
                    ? contribucionAvance.divide(horasTrabajador, 4, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            EstandarRendimiento estandar = estandarDominante(avances);

            BigDecimal rendimientoEsperado;
            String unidadMedida;
            String semaforo;
            BigDecimal desviacion;
            if (estandar != null && estandar.getRendimientoEsperado() != null
                    && estandar.getRendimientoEsperado().signum() > 0) {
                rendimientoEsperado = estandar.getRendimientoEsperado();
                unidadMedida = estandar.getUnidadMedida() != null
                        ? estandar.getUnidadMedida().name() : null;
                desviacion = rendimientoReal.subtract(rendimientoEsperado)
                        .divide(rendimientoEsperado, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
                semaforo = clasificarSemaforo(desviacion);
            } else {
                rendimientoEsperado = null;
                unidadMedida = null;
                desviacion = null;
                semaforo = "SIN_ESTANDAR";
            }

            Trabajador t = regsTrabajador.get(0).getAsignacionTrabajadorProyecto().getTrabajador();
            Proyecto   p = regsTrabajador.get(0).getAsignacionTrabajadorProyecto().getProyecto();
            String nombreCuadrilla = nombreCuadrillaDeRegistros(regsTrabajador);

            resultados.add(RendimientoIndicador.builder()
                    .idTrabajador(t != null ? t.getIdTrabajador() : null)
                    .nombreTrabajador(t != null ? t.getNombreTrabajador() : null)
                    .idProyecto(p != null ? p.getIdProyecto() : null)
                    .nombreProyecto(p != null ? p.getNombreProyecto() : null)
                    .idCuadrilla(clave.idCuadrilla)
                    .nombreCuadrilla(nombreCuadrilla)
                    .periodoInicio(inicio.toString())
                    .periodoFin(fin.toString())
                    .totalHorasTrabajadas(horasTrabajador)
                    .totalAvanceEjecutado(contribucionAvance.setScale(2, RoundingMode.HALF_UP))
                    .unidadMedida(unidadMedida)
                    .rendimientoReal(rendimientoReal)
                    .rendimientoEsperado(rendimientoEsperado)
                    .porcentajeDesviacion(desviacion)
                    .indicadorSemaforo(semaforo)
                    .build());
        }
        return resultados;
    }

    public List<RendimientoIndicador> indicadoresPorProyecto(Integer idProyecto,
                                                             LocalDate inicio,
                                                             LocalDate fin) {
        List<RegistroHoras> registros = registroHorasRepo
                .findByProyectoAndFechaBetween(idProyecto, inicio, fin);

        // Agrupar por trabajador para producir un indicador por cada uno
        Map<Integer, List<RegistroHoras>> porTrabajador = new LinkedHashMap<>();
        for (RegistroHoras r : registros) {
            Trabajador t = r.getAsignacionTrabajadorProyecto().getTrabajador();
            if (t == null || t.getIdTrabajador() == null) continue;
            porTrabajador.computeIfAbsent(t.getIdTrabajador(), k -> new ArrayList<>()).add(r);
        }

        List<RendimientoIndicador> resultados = new ArrayList<>();
        for (Integer idTrab : porTrabajador.keySet()) {
            resultados.addAll(indicadoresPorTrabajador(idTrab, inicio, fin)
                    .stream()
                    .filter(i -> Objects.equals(i.getIdProyecto(), idProyecto))
                    .toList());
        }
        return resultados;
    }

    // ─── HELPERS ──────────────────────────────────────────────────────────────

    private Map<ClaveGrupo, List<RegistroHoras>> agruparRegistros(List<RegistroHoras> regs) {
        Map<ClaveGrupo, List<RegistroHoras>> m = new LinkedHashMap<>();
        for (RegistroHoras r : regs) {
            Proyecto p = r.getAsignacionTrabajadorProyecto().getProyecto();
            if (p == null || p.getIdProyecto() == null) continue;
            ClaveGrupo k = new ClaveGrupo(p.getIdProyecto(), idCuadrilla(r));
            m.computeIfAbsent(k, x -> new ArrayList<>()).add(r);
        }
        return m;
    }

    private Integer idCuadrilla(RegistroHoras r) {
        Cuadrilla c = r.getCuadrilla();
        return c != null ? c.getIdCuadrilla() : null;
    }

    private Integer idCuadrillaDeAvance(AvancePartida a) {
        Cuadrilla c = a.getCuadrilla();
        return c != null ? c.getIdCuadrilla() : null;
    }

    private BigDecimal sumarHoras(List<RegistroHoras> regs) {
        BigDecimal total = BigDecimal.ZERO;
        for (RegistroHoras r : regs) {
            if (r.getHorasTrabajadas() != null) total = total.add(r.getHorasTrabajadas());
        }
        return total;
    }

    private BigDecimal sumarCantidades(List<AvancePartida> avs) {
        BigDecimal total = BigDecimal.ZERO;
        for (AvancePartida a : avs) {
            if (a.getCantidadEjecutada() != null) total = total.add(a.getCantidadEjecutada());
        }
        return total;
    }

    /**
     * Devuelve el EstandarRendimiento dominante (el que aporta más cantidad
     * acumulada) entre los avances dados. null si ninguno tiene estandar.
     */
    private EstandarRendimiento estandarDominante(List<AvancePartida> avances) {
        Map<Integer, BigDecimal> cantidadPorEstandar = new HashMap<>();
        Map<Integer, EstandarRendimiento> ref = new HashMap<>();
        for (AvancePartida a : avances) {
            if (a.getEstandar() == null || a.getEstandar().getIdEstandar() == null) continue;
            Integer id = a.getEstandar().getIdEstandar();
            cantidadPorEstandar.merge(id,
                    a.getCantidadEjecutada() != null ? a.getCantidadEjecutada() : BigDecimal.ZERO,
                    BigDecimal::add);
            ref.put(id, a.getEstandar());
        }
        EstandarRendimiento dominante = null;
        BigDecimal max = BigDecimal.ZERO.subtract(BigDecimal.ONE);
        for (Map.Entry<Integer, BigDecimal> e : cantidadPorEstandar.entrySet()) {
            if (e.getValue().compareTo(max) > 0) {
                max = e.getValue();
                dominante = ref.get(e.getKey());
            }
        }
        return dominante;
    }

    private String nombreCuadrillaDeRegistros(List<RegistroHoras> regs) {
        for (RegistroHoras r : regs) {
            if (r.getCuadrilla() != null && r.getCuadrilla().getNombreCuadrilla() != null) {
                return r.getCuadrilla().getNombreCuadrilla();
            }
        }
        return null;
    }

    private String clasificarSemaforo(BigDecimal desviacionPct) {
        if (desviacionPct == null) return "SIN_ESTANDAR";
        double d = desviacionPct.doubleValue();
        if (d >= -10) return "VERDE";
        if (d >= -30) return "AMARILLO";
        return "ROJO";
    }

    /** Clave para agrupar por (proyecto, cuadrilla). idCuadrilla puede ser null. */
    private record ClaveGrupo(Integer idProyecto, Integer idCuadrilla) {}
}
