package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "registros_migratorios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroMigratorio {

    public enum CategoriaMigratoria {
        turismo, negocios, razones_humanitarias, transito, actividades_remuneradas
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_migratorio")
    private Integer idMigratorio;

    @Column(name = "folio_documento", nullable = false, unique = true, length = 50)
    private String folioDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private CategoriaMigratoria categoria;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "dias_vigencia")
    private Integer diasVigencia;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "permiso_trabajo", nullable = false)
    private Boolean permisoTrabajo = false;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}
