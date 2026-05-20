package com.example.demo.repository;

import com.example.demo.entity.RegistroHoras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroHorasRepository extends JpaRepository<RegistroHoras, Integer> {

    List<RegistroHoras> findByAsignacionTrabajadorProyecto_IdAsignacionTp(Integer idAsignacionTp);

    /**
     * Registros de horas de un trabajador en cualquier proyecto, dentro de un rango.
     */
    @Query("""
        select r from RegistroHoras r
        where r.asignacionTrabajadorProyecto.trabajador.idTrabajador = :idTrabajador
          and r.fechaRegistro between :inicio and :fin
        """)
    List<RegistroHoras> findByTrabajadorAndFechaBetween(
            @Param("idTrabajador") Integer idTrabajador,
            @Param("inicio") LocalDate inicio,
            @Param("fin")    LocalDate fin);

    /**
     * Registros de horas de todos los trabajadores en un proyecto, dentro de un rango.
     */
    @Query("""
        select r from RegistroHoras r
        where r.asignacionTrabajadorProyecto.proyecto.idProyecto = :idProyecto
          and r.fechaRegistro between :inicio and :fin
        """)
    List<RegistroHoras> findByProyectoAndFechaBetween(
            @Param("idProyecto") Integer idProyecto,
            @Param("inicio") LocalDate inicio,
            @Param("fin")    LocalDate fin);
}
