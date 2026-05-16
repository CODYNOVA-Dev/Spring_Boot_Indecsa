package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Trabajador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trabajador {

    public enum EstadoTrabajador {
        DESCANSO, VACACIONES, INCAPACIDAD, ACTIVO, INACTIVO, BAJA, BOLETINADO
    }

    public enum Sexo { Masculino, Femenino, Otro }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trabajador")
    private Integer idTrabajador;

    @Column(name = "nombre_trabajador", nullable = false, length = 100)
    private String nombreTrabajador;

    @Column(name = "curp", nullable = false, unique = true, length = 18)
    private String curp;

    @Column(name = "rfc", nullable = false, unique = true, length = 13)
    private String rfc;

    @Column(name = "nss_trabajador", unique = true, length = 11)
    private String nssTrabajador;

    @Column(name = "nacionalidad", nullable = false, length = 100)
    private String nacionalidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_migratorio")
    private RegistroMigratorio registroMigratorio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_domicilio", nullable = false)
    private Domicilio domicilio;

    @Column(name = "foto_perfil_url", length = 255)
    private String fotoPerfilUrl;

    @Column(name = "puesto", nullable = false, length = 100)
    private String puesto;

    @Column(name = "desc_puesto", nullable = false, length = 500)
    private String descPuesto;

    @Column(name = "especialidad_trabajador", nullable = false, length = 100)
    private String especialidadTrabajador;

    @Column(name = "escolaridad", nullable = false, length = 100)
    private String escolaridad;

    @Column(name = "experiencia", length = 200)
    private String experiencia;

    @Column(name = "telefono_trabajador", nullable = false, length = 15)
    private String telefonoTrabajador;

    @Column(name = "correo_trabajador", nullable = false, unique = true, length = 100)
    private String correoTrabajador;

    @Column(name = "contratacion", nullable = false, length = 200)
    private String contratacion;

    @Column(name = "jornada", nullable = false, length = 200)
    private String jornada;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_trabajador", nullable = false)
    private EstadoTrabajador estadoTrabajador;

    @Column(name = "evaluacion_trabajador")
    private Byte evaluacionTrabajador;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado_calidad_vida", nullable = false)
    private Estado estadoCalidadVida;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", nullable = false)
    private Sexo sexo;

    @Column(name = "ant_penal", length = 500)
    private String antPenal;

    @Column(name = "deudor_alim", length = 500)
    private String deudorAlim;

    @Column(name = "folio_lic_cond", length = 20)
    private String folioLicCond;

    @Column(name = "estado_civil", length = 50)
    private String estadoCivil;

    @Column(name = "idiomas", length = 200)
    private String idiomas;

    @Column(name = "lengua_indigena", length = 100)
    private String lenguaIndigena;
}
