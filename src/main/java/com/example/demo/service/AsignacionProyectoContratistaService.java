package com.example.demo.service;

import com.example.demo.entity.AsignacionProyectoContratista;
import com.example.demo.entity.Contratista;
import com.example.demo.entity.Proyecto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AsignacionProyectoContratistaRepository;
import com.example.demo.repository.ContratistaRepository;
import com.example.demo.repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsignacionProyectoContratistaService {

    private final AsignacionProyectoContratistaRepository repo;
    private final ProyectoRepository proyectoRepo;
    private final ContratistaRepository contratistaRepo;

    public List<AsignacionProyectoContratista> findAll() { return repo.findAll(); }

    public List<AsignacionProyectoContratista> findByProyecto(Integer idProyecto) {
        return repo.findByProyecto_IdProyecto(idProyecto);
    }

    public List<AsignacionProyectoContratista> findByContratista(Integer idContratista) {
        return repo.findByContratista_IdContratista(idContratista);
    }

    public AsignacionProyectoContratista findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("AsignacionPC no encontrada: " + id));
    }

    public AsignacionProyectoContratista create(AsignacionProyectoContratista a) {
        a.setIdAsignacionPc(null);
        a.setProyecto(resolveProyecto(a.getProyecto()));
        a.setContratista(resolveContratista(a.getContratista()));
        if (a.getEstatusContrato() == null) a.setEstatusContrato(AsignacionProyectoContratista.EstatusContrato.VIGENTE);
        return repo.save(a);
    }

    public AsignacionProyectoContratista update(Integer id, AsignacionProyectoContratista a) {
        AsignacionProyectoContratista db = findById(id);
        db.setNumeroContrato(a.getNumeroContrato());
        db.setFechaInicio(a.getFechaInicio());
        db.setFechaFinEstimada(a.getFechaFinEstimada());
        if (a.getPersonalAsignado() != null) db.setPersonalAsignado(a.getPersonalAsignado());
        if (a.getEstatusContrato() != null) db.setEstatusContrato(a.getEstatusContrato());
        db.setObservaciones(a.getObservaciones());
        if (a.getProyecto() != null) db.setProyecto(resolveProyecto(a.getProyecto()));
        if (a.getContratista() != null) db.setContratista(resolveContratista(a.getContratista()));
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }

    private Proyecto resolveProyecto(Proyecto p) {
        if (p == null || p.getIdProyecto() == null) throw new IllegalArgumentException("id_proyecto requerido");
        return proyectoRepo.findById(p.getIdProyecto())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado: " + p.getIdProyecto()));
    }

    private Contratista resolveContratista(Contratista c) {
        if (c == null || c.getIdContratista() == null) throw new IllegalArgumentException("id_contratista requerido");
        return contratistaRepo.findById(c.getIdContratista())
                .orElseThrow(() -> new ResourceNotFoundException("Contratista no encontrado: " + c.getIdContratista()));
    }
}
