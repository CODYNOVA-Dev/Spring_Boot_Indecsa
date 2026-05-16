package com.example.demo.service;

import com.example.demo.entity.Rol;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository repo;

    public List<Rol> findAll() { return repo.findAll(); }

    public Rol findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + id));
    }

    public Rol create(Rol r) { r.setIdRol(null); return repo.save(r); }

    public Rol update(Integer id, Rol r) {
        Rol db = findById(id);
        db.setNombreRol(r.getNombreRol());
        db.setDescripcionRol(r.getDescripcionRol());
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }
}
