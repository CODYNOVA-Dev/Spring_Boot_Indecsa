package com.example.demo.service;

import com.example.demo.entity.RegistroMigratorio;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RegistroMigratorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistroMigratorioService {

    private final RegistroMigratorioRepository repo;

    public List<RegistroMigratorio> findAll() { return repo.findAll(); }

    public RegistroMigratorio findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("RegistroMigratorio no encontrado: " + id));
    }

    public RegistroMigratorio create(RegistroMigratorio r) {
        r.setIdMigratorio(null);
        if (r.getPermisoTrabajo() == null) r.setPermisoTrabajo(false);
        if (r.getActivo() == null) r.setActivo(true);
        return repo.save(r);
    }

    public RegistroMigratorio update(Integer id, RegistroMigratorio r) {
        RegistroMigratorio db = findById(id);
        db.setFolioDocumento(r.getFolioDocumento());
        db.setCategoria(r.getCategoria());
        db.setFechaEmision(r.getFechaEmision());
        db.setDiasVigencia(r.getDiasVigencia());
        db.setFechaVencimiento(r.getFechaVencimiento());
        if (r.getPermisoTrabajo() != null) db.setPermisoTrabajo(r.getPermisoTrabajo());
        if (r.getActivo() != null) db.setActivo(r.getActivo());
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }
}
