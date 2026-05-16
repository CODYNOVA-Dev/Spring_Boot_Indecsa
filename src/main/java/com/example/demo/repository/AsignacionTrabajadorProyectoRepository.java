package com.example.demo.repository;

import com.example.demo.entity.AsignacionTrabajadorProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionTrabajadorProyectoRepository extends JpaRepository<AsignacionTrabajadorProyecto, Integer> {
    List<AsignacionTrabajadorProyecto> findByProyecto_IdProyecto(Integer idProyecto);
    List<AsignacionTrabajadorProyecto> findByTrabajador_IdTrabajador(Integer idTrabajador);
}
