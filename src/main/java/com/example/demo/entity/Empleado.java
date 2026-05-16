package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Empleado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(name = "nombre_empleado", nullable = false, length = 100)
    private String nombreEmpleado;

    @Column(name = "curp", nullable = false, unique = true, length = 18)
    private String curp;

    @Column(name = "correo_empleado", nullable = false, unique = true, length = 100)
    private String correoEmpleado;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "foto_perfil_url", length = 255)
    private String fotoPerfilUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;
}
