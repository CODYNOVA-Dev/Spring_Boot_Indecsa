package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Contratista")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contratista {

    public enum EstadoContratista { ACTIVO, INACTIVO, SUSPENDIDO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contratista")
    private Integer idContratista;

    @Column(name = "nombre_contratista", nullable = false, length = 100)
    private String nombreContratista;

    @Column(name = "curp", nullable = false, length = 18)
    private String curp;

    @Column(name = "rfc_contratista", nullable = false, unique = true, length = 15)
    private String rfcContratista;

    @Column(name = "telefono_contratista", nullable = false, length = 15)
    private String telefonoContratista;

    @Column(name = "correo_contratista", nullable = false, unique = true, length = 100)
    private String correoContratista;

    @Column(name = "descripcion_contratista", nullable = false, length = 255)
    private String descripcionContratista;

    @Column(name = "foto_perfil_url", length = 255)
    private String fotoPerfilUrl;

    @Column(name = "experiencia", length = 200)
    private String experiencia;

    @Column(name = "calificacion_contratista")
    private Byte calificacionContratista;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_contratista", nullable = false)
    private EstadoContratista estadoContratista;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado_operacion", nullable = false)
    private Estado estadoOperacion;
}
