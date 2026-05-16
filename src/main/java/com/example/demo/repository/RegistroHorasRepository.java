package com.example.demo.repository;

import com.example.demo.entity.RegistroHoras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroHorasRepository extends JpaRepository<RegistroHoras, Integer> {
    List<RegistroHoras> findByAsignacionTrabajadorProyecto_IdAsignacionTp(Integer idAsignacionTp);
}
