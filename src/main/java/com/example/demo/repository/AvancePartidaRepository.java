package com.example.demo.repository;

import com.example.demo.entity.AvancePartida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvancePartidaRepository extends JpaRepository<AvancePartida, Integer> {

    List<AvancePartida> findByProyecto_IdProyecto(Integer idProyecto);

    /**
     * Avances de un proyecto dentro de un rango de fechas.
     */
    @Query("""
        select a from AvancePartida a
        where a.proyecto.idProyecto = :idProyecto
          and a.fechaRegistro between :inicio and :fin
        """)
    List<AvancePartida> findByProyectoAndFechaBetween(
            @Param("idProyecto") Integer idProyecto,
            @Param("inicio") LocalDate inicio,
            @Param("fin")    LocalDate fin);
}
