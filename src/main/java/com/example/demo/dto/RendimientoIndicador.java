package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Indicador agregado de rendimiento para una combinación
 * (trabajador, proyecto, cuadrilla) en un rango de fechas.
 *
 * - rendimientoReal = totalAvanceEjecutado / totalHorasTrabajadas
 * - rendimientoEsperado viene del EstandarRendimiento del avance dominante
 * - porcentajeDesviacion = (real - esperado) / esperado * 100
 * - indicadorSemaforo:
 *     VERDE   si desviacion >= -10
 *     AMARILLO si -30 <= desviacion < -10
 *     ROJO    si desviacion < -30
 *     SIN_ESTANDAR si no hay estandar de referencia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendimientoIndicador {

    private Integer idTrabajador;
    private String  nombreTrabajador;
    private Integer idProyecto;
    private String  nombreProyecto;
    private Integer idCuadrilla;
    private String  nombreCuadrilla;
    private String  periodoInicio;
    private String  periodoFin;
    private BigDecimal totalHorasTrabajadas;
    private BigDecimal totalAvanceEjecutado;
    private String     unidadMedida;
    private BigDecimal rendimientoReal;
    private BigDecimal rendimientoEsperado;
    private BigDecimal porcentajeDesviacion;
    private String     indicadorSemaforo;
}
