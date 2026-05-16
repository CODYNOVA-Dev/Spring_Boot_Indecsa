package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Asignacion_Proyecto_Contratista")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionProyectoContratista {

    public enum EstatusContrato {
        ACTIVO, VIGENTE, SUSPENDIDO, FINALIZADO, CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion_pc")
    private Integer idAsignacionPc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_contratista", nullable = false)
    private Contratista contratista;

    @Column(name = "numero_contrato", length = 50)
    private String numeroContrato;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_estimada")
    private LocalDate fechaFinEstimada;

    @Column(name = "personal_asignado", nullable = false)
    private Integer personalAsignado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus_contrato", nullable = false)
    private EstatusContrato estatusContrato;

    @Column(name = "observaciones", length = 500)
    private String observaciones;
}
