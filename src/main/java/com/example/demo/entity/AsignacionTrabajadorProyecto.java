package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Asignacion_Trabajador_Proyecto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionTrabajadorProyecto {

    public enum EstatusAsignacion {
        ACTIVO, SUSPENDIDO, INCAPACIDAD, CANCELADO, VACACIONES, FINALIZADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion_tp")
    private Integer idAsignacionTp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_trabajador", nullable = false)
    private Trabajador trabajador;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_asignacion_pc", nullable = false)
    private AsignacionProyectoContratista asignacionProyectoContratista;

    @Column(name = "puesto_en_proyecto", length = 100)
    private String puestoEnProyecto;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_estimada")
    private LocalDate fechaFinEstimada;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus_asignacion", nullable = false)
    private EstatusAsignacion estatusAsignacion;
}
