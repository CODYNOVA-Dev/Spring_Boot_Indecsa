package com.example.demo.service;

import com.example.demo.entity.Contratista;
import com.example.demo.entity.Estado;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ContratistaRepository;
import com.example.demo.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratistaService {

    private final ContratistaRepository repo;
    private final EstadoRepository estadoRepo;

    public List<Contratista> findAll() { return repo.findAll(); }

    public Contratista findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contratista no encontrado: " + id));
    }

    public Contratista create(Contratista c) {
        c.setIdContratista(null);
        c.setEstadoOperacion(resolveEstado(c.getEstadoOperacion()));
        if (c.getEstadoContratista() == null) c.setEstadoContratista(Contratista.EstadoContratista.ACTIVO);
        return repo.save(c);
    }

    public Contratista update(Integer id, Contratista c) {
        Contratista db = findById(id);
        db.setNombreContratista(c.getNombreContratista());
        db.setCurp(c.getCurp());
        db.setRfcContratista(c.getRfcContratista());
        db.setTelefonoContratista(c.getTelefonoContratista());
        db.setCorreoContratista(c.getCorreoContratista());
        db.setDescripcionContratista(c.getDescripcionContratista());
        db.setFotoPerfilUrl(c.getFotoPerfilUrl());
        db.setExperiencia(c.getExperiencia());
        db.setCalificacionContratista(c.getCalificacionContratista());
        if (c.getEstadoContratista() != null) db.setEstadoContratista(c.getEstadoContratista());
        if (c.getEstadoOperacion() != null) db.setEstadoOperacion(resolveEstado(c.getEstadoOperacion()));
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }

    private Estado resolveEstado(Estado e) {
        if (e == null || e.getIdEstado() == null) throw new IllegalArgumentException("id_estado_operacion requerido");
        return estadoRepo.findById(e.getIdEstado())
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado: " + e.getIdEstado()));
    }
}
