package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Avance_Partida")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvancePartida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avance")
    private Integer idAvance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cuadrilla")
    private Cuadrilla cuadrilla;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estandar")
    private EstandarRendimiento estandar;

    @Column(name = "nombre_partida", nullable = false, length = 200)
    private String nombrePartida;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "cantidad_ejecutada", nullable = false, precision = 12, scale = 4)
    private BigDecimal cantidadEjecutada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_empleado_registro", nullable = false)
    private Empleado empleadoRegistro;
}
