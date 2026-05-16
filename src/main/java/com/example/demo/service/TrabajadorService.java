package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrabajadorService {

    private final TrabajadorRepository repo;
    private final DomicilioRepository domicilioRepo;
    private final EstadoRepository estadoRepo;
    private final RegistroMigratorioRepository migratorioRepo;

    public List<Trabajador> findAll() { return repo.findAll(); }

    public Trabajador findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado: " + id));
    }

    public Trabajador create(Trabajador t) {
        t.setIdTrabajador(null);
        t.setDomicilio(resolveDomicilio(t.getDomicilio()));
        t.setEstadoCalidadVida(resolveEstado(t.getEstadoCalidadVida(), "id_estado_calidad_vida"));
        if (t.getRegistroMigratorio() != null && t.getRegistroMigratorio().getIdMigratorio() != null) {
            t.setRegistroMigratorio(resolveMigratorio(t.getRegistroMigratorio()));
        } else {
            t.setRegistroMigratorio(null);
        }
        if (t.getEstadoTrabajador() == null) t.setEstadoTrabajador(Trabajador.EstadoTrabajador.ACTIVO);
        return repo.save(t);
    }

    public Trabajador update(Integer id, Trabajador t) {
        Trabajador db = findById(id);
        db.setNombreTrabajador(t.getNombreTrabajador());
        db.setCurp(t.getCurp());
        db.setRfc(t.getRfc());
        db.setNssTrabajador(t.getNssTrabajador());
        db.setNacionalidad(t.getNacionalidad());
        db.setFotoPerfilUrl(t.getFotoPerfilUrl());
        db.setPuesto(t.getPuesto());
        db.setDescPuesto(t.getDescPuesto());
        db.setEspecialidadTrabajador(t.getEspecialidadTrabajador());
        db.setEscolaridad(t.getEscolaridad());
        db.setExperiencia(t.getExperiencia());
        db.setTelefonoTrabajador(t.getTelefonoTrabajador());
        db.setCorreoTrabajador(t.getCorreoTrabajador());
        db.setContratacion(t.getContratacion());
        db.setJornada(t.getJornada());
        if (t.getEstadoTrabajador() != null) db.setEstadoTrabajador(t.getEstadoTrabajador());
        db.setEvaluacionTrabajador(t.getEvaluacionTrabajador());
        db.setFechaIngreso(t.getFechaIngreso());
        if (t.getSexo() != null) db.setSexo(t.getSexo());
        db.setAntPenal(t.getAntPenal());
        db.setDeudorAlim(t.getDeudorAlim());
        db.setFolioLicCond(t.getFolioLicCond());
        db.setEstadoCivil(t.getEstadoCivil());
        db.setIdiomas(t.getIdiomas());
        db.setLenguaIndigena(t.getLenguaIndigena());
        if (t.getDomicilio() != null) db.setDomicilio(resolveDomicilio(t.getDomicilio()));
        if (t.getEstadoCalidadVida() != null) db.setEstadoCalidadVida(resolveEstado(t.getEstadoCalidadVida(), "id_estado_calidad_vida"));
        if (t.getRegistroMigratorio() != null && t.getRegistroMigratorio().getIdMigratorio() != null) {
            db.setRegistroMigratorio(resolveMigratorio(t.getRegistroMigratorio()));
        }
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }

    private Domicilio resolveDomicilio(Domicilio d) {
        if (d == null || d.getIdDomicilio() == null) throw new IllegalArgumentException("id_domicilio requerido");
        return domicilioRepo.findById(d.getIdDomicilio())
                .orElseThrow(() -> new ResourceNotFoundException("Domicilio no encontrado: " + d.getIdDomicilio()));
    }

    private Estado resolveEstado(Estado e, String campo) {
        if (e == null || e.getIdEstado() == null) throw new IllegalArgumentException(campo + " requerido");
        return estadoRepo.findById(e.getIdEstado())
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado: " + e.getIdEstado()));
    }

    private RegistroMigratorio resolveMigratorio(RegistroMigratorio r) {
        return migratorioRepo.findById(r.getIdMigratorio())
                .orElseThrow(() -> new ResourceNotFoundException("RegistroMigratorio no encontrado: " + r.getIdMigratorio()));
    }
}
