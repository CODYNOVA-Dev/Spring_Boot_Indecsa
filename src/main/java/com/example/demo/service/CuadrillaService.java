package com.example.demo.service;

import com.example.demo.entity.Cuadrilla;
import com.example.demo.entity.Proyecto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CuadrillaRepository;
import com.example.demo.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuadrillaService {

    private final CuadrillaRepository repo;
    private final ProyectoRepository proyectoRepo;

    public List<Cuadrilla> findAll() { return repo.findAll(); }

    public List<Cuadrilla> findByProyecto(Integer idProyecto) { return repo.findByProyecto_IdProyecto(idProyecto); }

    public Cuadrilla findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cuadrilla no encontrada: " + id));
    }

    public Cuadrilla create(Cuadrilla c) {
        c.setIdCuadrilla(null);
        c.setProyecto(resolveProyecto(c.getProyecto()));
        if (c.getEstatusCuadrilla() == null) c.setEstatusCuadrilla(Cuadrilla.EstatusCuadrilla.ACTIVO);
        return repo.save(c);
    }

    public Cuadrilla update(Integer id, Cuadrilla c) {
        Cuadrilla db = findById(id);
        db.setNombreCuadrilla(c.getNombreCuadrilla());
        db.setFrenteTrabajo(c.getFrenteTrabajo());
        if (c.getEstatusCuadrilla() != null) db.setEstatusCuadrilla(c.getEstatusCuadrilla());
        if (c.getProyecto() != null) db.setProyecto(resolveProyecto(c.getProyecto()));
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }

    private Proyecto resolveProyecto(Proyecto p) {
        if (p == null || p.getIdProyecto() == null) throw new IllegalArgumentException("id_proyecto requerido");
        return proyectoRepo.findById(p.getIdProyecto())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado: " + p.getIdProyecto()));
    }
}
