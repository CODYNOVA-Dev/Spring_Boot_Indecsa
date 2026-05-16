package com.example.demo.service;

import com.example.demo.entity.AsignacionProyectoContratista;
import com.example.demo.entity.AsignacionTrabajadorProyecto;
import com.example.demo.entity.Proyecto;
import com.example.demo.entity.Trabajador;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AsignacionProyectoContratistaRepository;
import com.example.demo.repository.AsignacionTrabajadorProyectoRepository;
import com.example.demo.repository.ProyectoRepository;
import com.example.demo.repository.TrabajadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsignacionTrabajadorProyectoService {

    private final AsignacionTrabajadorProyectoRepository repo;
    private final TrabajadorRepository trabajadorRepo;
    private final ProyectoRepository proyectoRepo;
    private final AsignacionProyectoContratistaRepository asignacionPcRepo;

    public List<AsignacionTrabajadorProyecto> findAll() { return repo.findAll(); }

    public List<AsignacionTrabajadorProyecto> findByProyecto(Integer idProyecto) {
        return repo.findByProyecto_IdProyecto(idProyecto);
    }

    public List<AsignacionTrabajadorProyecto> findByTrabajador(Integer idTrabajador) {
        return repo.findByTrabajador_IdTrabajador(idTrabajador);
    }

    public AsignacionTrabajadorProyecto findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("AsignacionTP no encontrada: " + id));
    }

    public AsignacionTrabajadorProyecto create(AsignacionTrabajadorProyecto a) {
        a.setIdAsignacionTp(null);
        a.setTrabajador(resolveTrabajador(a.getTrabajador()));
        a.setProyecto(resolveProyecto(a.getProyecto()));
        a.setAsignacionProyectoContratista(resolveAsignacionPc(a.getAsignacionProyectoContratista()));
        if (a.getEstatusAsignacion() == null) a.setEstatusAsignacion(AsignacionTrabajadorProyecto.EstatusAsignacion.ACTIVO);
        return repo.save(a);
    }

    public AsignacionTrabajadorProyecto update(Integer id, AsignacionTrabajadorProyecto a) {
        AsignacionTrabajadorProyecto db = findById(id);
        db.setPuestoEnProyecto(a.getPuestoEnProyecto());
        db.setFechaInicio(a.getFechaInicio());
        db.setFechaFinEstimada(a.getFechaFinEstimada());
        if (a.getEstatusAsignacion() != null) db.setEstatusAsignacion(a.getEstatusAsignacion());
        if (a.getTrabajador() != null) db.setTrabajador(resolveTrabajador(a.getTrabajador()));
        if (a.getProyecto() != null) db.setProyecto(resolveProyecto(a.getProyecto()));
        if (a.getAsignacionProyectoContratista() != null) {
            db.setAsignacionProyectoContratista(resolveAsignacionPc(a.getAsignacionProyectoContratista()));
        }
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }

    private Trabajador resolveTrabajador(Trabajador t) {
        if (t == null || t.getIdTrabajador() == null) throw new IllegalArgumentException("id_trabajador requerido");
        return trabajadorRepo.findById(t.getIdTrabajador())
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado: " + t.getIdTrabajador()));
    }

    private Proyecto resolveProyecto(Proyecto p) {
        if (p == null || p.getIdProyecto() == null) throw new IllegalArgumentException("id_proyecto requerido");
        return proyectoRepo.findById(p.getIdProyecto())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado: " + p.getIdProyecto()));
    }

    private AsignacionProyectoContratista resolveAsignacionPc(AsignacionProyectoContratista a) {
        if (a == null || a.getIdAsignacionPc() == null) throw new IllegalArgumentException("id_asignacion_pc requerido");
        return asignacionPcRepo.findById(a.getIdAsignacionPc())
                .orElseThrow(() -> new ResourceNotFoundException("AsignacionPC no encontrada: " + a.getIdAsignacionPc()));
    }
}
