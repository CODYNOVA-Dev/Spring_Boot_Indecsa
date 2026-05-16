package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    public enum NombreRol { ADMIN, CAPITAL_HUMANO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre_rol", nullable = false, unique = true)
    private NombreRol nombreRol;

    @Column(name = "descripcion_rol", length = 255)
    private String descripcionRol;
}
