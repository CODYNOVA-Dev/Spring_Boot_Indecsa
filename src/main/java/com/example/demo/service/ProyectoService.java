package com.example.demo.service;

import com.example.demo.entity.Domicilio;
import com.example.demo.entity.Proyecto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DomicilioRepository;
import com.example.demo.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository repo;
    private final DomicilioRepository domicilioRepo;

    public List<Proyecto> findAll() { return repo.findAll(); }

    public Proyecto findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado: " + id));
    }

    public Proyecto create(Proyecto p) {
        p.setIdProyecto(null);
        p.setDomicilio(resolveDomicilio(p.getDomicilio()));
        if (p.getEstatusProyecto() == null) p.setEstatusProyecto(Proyecto.EstatusProyecto.PLANEACION);
        return repo.save(p);
    }

    public Proyecto update(Integer id, Proyecto p) {
        Proyecto db = findById(id);
        db.setNombreProyecto(p.getNombreProyecto());
        db.setTipoProyecto(p.getTipoProyecto());
        db.setOfertaTrabajo(p.getOfertaTrabajo());
        db.setCliente(p.getCliente());
        db.setFechaEstimadaInicio(p.getFechaEstimadaInicio());
        db.setFechaEstimadaFin(p.getFechaEstimadaFin());
        db.setCalificacionProyecto(p.getCalificacionProyecto());
        if (p.getEstatusProyecto() != null) db.setEstatusProyecto(p.getEstatusProyecto());
        db.setDescripcionProyecto(p.getDescripcionProyecto());
        db.setImagenProyectoUrl(p.getImagenProyectoUrl());
        if (p.getDomicilio() != null) db.setDomicilio(resolveDomicilio(p.getDomicilio()));
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }

    private Domicilio resolveDomicilio(Domicilio d) {
        if (d == null || d.getIdDomicilio() == null) throw new IllegalArgumentException("id_domicilio requerido");
        return domicilioRepo.findById(d.getIdDomicilio())
                .orElseThrow(() -> new ResourceNotFoundException("Domicilio no encontrado: " + d.getIdDomicilio()));
    }
}
