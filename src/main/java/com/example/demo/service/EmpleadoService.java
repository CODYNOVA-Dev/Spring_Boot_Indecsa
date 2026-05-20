package com.example.demo.service;

import com.example.demo.entity.Empleado;
import com.example.demo.entity.Rol;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EmpleadoRepository;
import com.example.demo.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository repo;
    private final RolRepository      rolRepo;
    private final PasswordEncoder    encoder;

    public List<Empleado> findAll() { return repo.findAll(); }

    public Empleado findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado: " + id));
    }

    public Empleado create(Empleado e) {
        e.setIdEmpleado(null);
        e.setRol(resolveRol(e.getRol()));
        if (e.getContrasena() == null || e.getContrasena().isBlank()) {
            throw new IllegalArgumentException("contrasena requerida");
        }
        e.setContrasena(hashSiHaceFalta(e.getContrasena()));
        return repo.save(e);
    }

    public Empleado update(Integer id, Empleado e) {
        Empleado db = findById(id);
        db.setNombreEmpleado(e.getNombreEmpleado());
        db.setCurp(e.getCurp());
        db.setCorreoEmpleado(e.getCorreoEmpleado());
        if (e.getContrasena() != null && !e.getContrasena().isBlank()) {
            db.setContrasena(hashSiHaceFalta(e.getContrasena()));
        }
        db.setFotoPerfilUrl(e.getFotoPerfilUrl());
        if (e.getRol() != null) db.setRol(resolveRol(e.getRol()));
        return repo.save(db);
    }

    public void delete(Integer id) { repo.delete(findById(id)); }

    private Rol resolveRol(Rol r) {
        if (r == null || r.getIdRol() == null) throw new IllegalArgumentException("id_rol requerido");
        return rolRepo.findById(r.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + r.getIdRol()));
    }

    /**
     * Si ya viene hasheado (BCrypt empieza con $2 y mide 60 chars) lo deja igual.
     * En cualquier otro caso lo hashea con el encoder configurado.
     * Esto evita doble-hash si el cliente envía un hash existente al actualizar.
     */
    private String hashSiHaceFalta(String pass) {
        if (pass.startsWith("$2") && pass.length() == 60) return pass;
        return encoder.encode(pass);
    }
}
