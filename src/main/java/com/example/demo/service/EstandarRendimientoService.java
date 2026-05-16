package com.example.demo.service;

import com.example.demo.entity.EstandarRendimiento;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EstandarRendimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstandarRendimientoService {

    private final EstandarRendimientoRepository repo;

    public List<EstandarRendimiento> findAll() { return repo.findAll(); }

    public EstandarRendimiento findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("EstandarRendimiento no encontrado: " + id));
    }

    public EstandarRendimiento create(EstandarRendimiento e) { e.setIdEstandar(null); return repo.save(e); }

    public EstandarRendimiento update(Integer id, EstandarRendimiento e) {
        EstandarRendimiento db = findById(id);
        db.setNombreActividad(e.getNombreActividad());
        if (e.getUnidadMedida() != null) db.setUnidadMedida(e.getUnidadMedida());
        db.setRendimientoEsperado(e.getRendimientoEsperado());
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }
}
