package com.example.demo.repository;

import com.example.demo.entity.AsignacionProyectoContratista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionProyectoContratistaRepository extends JpaRepository<AsignacionProyectoContratista, Integer> {
    List<AsignacionProyectoContratista> findByProyecto_IdProyecto(Integer idProyecto);
    List<AsignacionProyectoContratista> findByContratista_IdContratista(Integer idContratista);
}
