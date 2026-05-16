package com.example.demo.repository;

import com.example.demo.entity.Trabajador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Integer> {
    Optional<Trabajador> findByCurp(String curp);
    Optional<Trabajador> findByRfc(String rfc);
    Optional<Trabajador> findByCorreoTrabajador(String correo);
}
