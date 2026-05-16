package com.example.demo.repository;

import com.example.demo.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    Optional<Empleado> findByCorreoEmpleado(String correoEmpleado);
    Optional<Empleado> findByCurp(String curp);
}
