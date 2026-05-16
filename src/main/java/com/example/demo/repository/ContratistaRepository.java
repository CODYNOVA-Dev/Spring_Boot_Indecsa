package com.example.demo.repository;

import com.example.demo.entity.Contratista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContratistaRepository extends JpaRepository<Contratista, Integer> {
    Optional<Contratista> findByRfcContratista(String rfc);
    Optional<Contratista> findByCorreoContratista(String correo);
}
