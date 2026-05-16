package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvancePartidaService {

    private final AvancePartidaRepository repo;
    private final ProyectoRepository proyectoRepo;
    private final CuadrillaRepository cuadrillaRepo;
    private final EstandarRendimientoRepository estandarRepo;
    private final EmpleadoRepository empleadoRepo;

    public List<AvancePartida> findAll() { return repo.findAll(); }

    public List<AvancePartida> findByProyecto(Integer idProyecto) { return repo.findByProyecto_IdProyecto(idProyecto); }

    public AvancePartida findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("AvancePartida no encontrada: " + id));
    }

    public AvancePartida create(AvancePartida a) {
        a.setIdAvance(null);
        a.setProyecto(resolveProyecto(a.getProyecto()));
        a.setEmpleadoRegistro(resolveEmpleado(a.getEmpleadoRegistro()));
        a.setCuadrilla(resolveCuadrillaOptional(a.getCuadrilla()));
        a.setEstandar(resolveEstandarOptional(a.getEstandar()));
        return repo.save(a);
    }

    public AvancePartida update(Integer id, AvancePartida a) {
        AvancePartida db = findById(id);
        db.setNombrePartida(a.getNombrePartida());
        db.setFechaRegistro(a.getFechaRegistro());
        db.setCantidadEjecutada(a.getCantidadEjecutada());
        if (a.getProyecto() != null) db.setProyecto(resolveProyecto(a.getProyecto()));
        if (a.getEmpleadoRegistro() != null) db.setEmpleadoRegistro(resolveEmpleado(a.getEmpleadoRegistro()));
        if (a.getCuadrilla() != null) db.setCuadrilla(resolveCuadrillaOptional(a.getCuadrilla()));
        if (a.getEstandar() != null) db.setEstandar(resolveEstandarOptional(a.getEstandar()));
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }

    private Proyecto resolveProyecto(Proyecto p) {
        if (p == null || p.getIdProyecto() == null) throw new IllegalArgumentException("id_proyecto requerido");
        return proyectoRepo.findById(p.getIdProyecto())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado: " + p.getIdProyecto()));
    }

    private Empleado resolveEmpleado(Empleado e) {
        if (e == null || e.getIdEmpleado() == null) throw new IllegalArgumentException("id_empleado_registro requerido");
        return empleadoRepo.findById(e.getIdEmpleado())
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado: " + e.getIdEmpleado()));
    }

    private Cuadrilla resolveCuadrillaOptional(Cuadrilla c) {
        if (c == null || c.getIdCuadrilla() == null) return null;
        return cuadrillaRepo.findById(c.getIdCuadrilla())
                .orElseThrow(() -> new ResourceNotFoundException("Cuadrilla no encontrada: " + c.getIdCuadrilla()));
    }

    private EstandarRendimiento resolveEstandarOptional(EstandarRendimiento e) {
        if (e == null || e.getIdEstandar() == null) return null;
        return estandarRepo.findById(e.getIdEstandar())
                .orElseThrow(() -> new ResourceNotFoundException("EstandarRendimiento no encontrado: " + e.getIdEstandar()));
    }
}
