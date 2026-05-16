package com.example.demo.repository;

import com.example.demo.entity.RegistroMigratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroMigratorioRepository extends JpaRepository<RegistroMigratorio, Integer> {
}
