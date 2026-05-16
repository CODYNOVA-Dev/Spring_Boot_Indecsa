package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Registro_Horas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroHoras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private Integer idRegistro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_asignacion_tp", nullable = false)
    private AsignacionTrabajadorProyecto asignacionTrabajadorProyecto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cuadrilla")
    private Cuadrilla cuadrilla;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "horas_trabajadas", nullable = false, precision = 5, scale = 2)
    private BigDecimal horasTrabajadas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_empleado_registro", nullable = false)
    private Empleado empleadoRegistro;
}
