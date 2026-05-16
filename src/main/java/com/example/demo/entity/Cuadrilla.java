package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Cuadrilla")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cuadrilla {

    public enum EstatusCuadrilla { ACTIVO, INACTIVO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuadrilla")
    private Integer idCuadrilla;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @Column(name = "nombre_cuadrilla", nullable = false, length = 100)
    private String nombreCuadrilla;

    @Column(name = "frente_trabajo", length = 200)
    private String frenteTrabajo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus_cuadrilla", nullable = false)
    private EstatusCuadrilla estatusCuadrilla;
}
