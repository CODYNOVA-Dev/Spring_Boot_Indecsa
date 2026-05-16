package com.example.demo.service;

import com.example.demo.entity.Domicilio;
import com.example.demo.entity.Estado;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DomicilioRepository;
import com.example.demo.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DomicilioService {

    private final DomicilioRepository repo;
    private final EstadoRepository estadoRepo;

    public List<Domicilio> findAll() { return repo.findAll(); }

    public Domicilio findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Domicilio no encontrado: " + id));
    }

    public Domicilio create(Domicilio d) {
        d.setIdDomicilio(null);
        d.setEstado(resolveEstado(d.getEstado()));
        return repo.save(d);
    }

    public Domicilio update(Integer id, Domicilio d) {
        Domicilio db = findById(id);
        db.setCalle(d.getCalle());
        db.setNumExt(d.getNumExt());
        db.setNumInt(d.getNumInt());
        db.setColonia(d.getColonia());
        db.setCodPost(d.getCodPost());
        db.setMunAlc(d.getMunAlc());
        if (d.getEstado() != null) db.setEstado(resolveEstado(d.getEstado()));
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }

    private Estado resolveEstado(Estado e) {
        if (e == null || e.getIdEstado() == null) throw new IllegalArgumentException("id_estado requerido");
        return estadoRepo.findById(e.getIdEstado())
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado: " + e.getIdEstado()));
    }
}
