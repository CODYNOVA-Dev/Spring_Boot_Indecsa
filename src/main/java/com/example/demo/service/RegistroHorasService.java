package com.example.demo.service;

import com.example.demo.entity.AsignacionTrabajadorProyecto;
import com.example.demo.entity.Cuadrilla;
import com.example.demo.entity.Empleado;
import com.example.demo.entity.RegistroHoras;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AsignacionTrabajadorProyectoRepository;
import com.example.demo.repository.CuadrillaRepository;
import com.example.demo.repository.EmpleadoRepository;
import com.example.demo.repository.RegistroHorasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistroHorasService {

    private final RegistroHorasRepository repo;
    private final AsignacionTrabajadorProyectoRepository asignacionTpRepo;
    private final CuadrillaRepository cuadrillaRepo;
    private final EmpleadoRepository empleadoRepo;

    public List<RegistroHoras> findAll() { return repo.findAll(); }

    public List<RegistroHoras> findByAsignacionTp(Integer idAsignacionTp) {
        return repo.findByAsignacionTrabajadorProyecto_IdAsignacionTp(idAsignacionTp);
    }

    public RegistroHoras findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("RegistroHoras no encontrado: " + id));
    }

    public RegistroHoras create(RegistroHoras r) {
        r.setIdRegistro(null);
        r.setAsignacionTrabajadorProyecto(resolveAsignacionTp(r.getAsignacionTrabajadorProyecto()));
        r.setEmpleadoRegistro(resolveEmpleado(r.getEmpleadoRegistro()));
        r.setCuadrilla(resolveCuadrillaOptional(r.getCuadrilla()));
        return repo.save(r);
    }

    public RegistroHoras update(Integer id, RegistroHoras r) {
        RegistroHoras db = findById(id);
        db.setFechaRegistro(r.getFechaRegistro());
        db.setHorasTrabajadas(r.getHorasTrabajadas());
        if (r.getAsignacionTrabajadorProyecto() != null) db.setAsignacionTrabajadorProyecto(resolveAsignacionTp(r.getAsignacionTrabajadorProyecto()));
        if (r.getEmpleadoRegistro() != null) db.setEmpleadoRegistro(resolveEmpleado(r.getEmpleadoRegistro()));
        if (r.getCuadrilla() != null) db.setCuadrilla(resolveCuadrillaOptional(r.getCuadrilla()));
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }

    private AsignacionTrabajadorProyecto resolveAsignacionTp(AsignacionTrabajadorProyecto a) {
        if (a == null || a.getIdAsignacionTp() == null) throw new IllegalArgumentException("id_asignacion_tp requerido");
        return asignacionTpRepo.findById(a.getIdAsignacionTp())
                .orElseThrow(() -> new ResourceNotFoundException("AsignacionTP no encontrada: " + a.getIdAsignacionTp()));
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
}
