package com.example.demo.service;

import com.example.demo.entity.Estado;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoService {

    private final EstadoRepository repo;

    public List<Estado> findAll() { return repo.findAll(); }

    public Estado findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado: " + id));
    }

    public Estado create(Estado e) { e.setIdEstado(null); return repo.save(e); }

    public Estado update(Integer id, Estado e) {
        Estado db = findById(id);
        db.setNombreEst(e.getNombreEst());
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }
}
