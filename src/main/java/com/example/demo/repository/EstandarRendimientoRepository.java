package com.example.demo.repository;

import com.example.demo.entity.EstandarRendimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstandarRendimientoRepository extends JpaRepository<EstandarRendimiento, Integer> {
}
