package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.entity.Empleado;
import com.example.demo.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmpleadoRepository empleadoRepo;
    private final PasswordEncoder    encoder;

    public LoginResponse login(LoginRequest req) {
        Empleado emp = empleadoRepo.findByCorreoEmpleado(req.getCorreoEmpleado())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));

        if (!verificarYMigrar(emp, req.getContrasena())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        return new LoginResponse(
                emp.getIdEmpleado(),
                emp.getNombreEmpleado(),
                emp.getCorreoEmpleado(),
                emp.getCurp(),
                emp.getFotoPerfilUrl(),
                emp.getRol() != null ? emp.getRol().getIdRol() : null,
                emp.getRol() != null && emp.getRol().getNombreRol() != null ? emp.getRol().getNombreRol().name() : null,
                emp.getRol() != null ? emp.getRol().getDescripcionRol() : null
        );
    }

    /**
     * Verifica la contraseña con dos rutas:
     *   - Hash BCrypt: valida con encoder.matches()
     *   - Texto plano legacy: compara directamente y migra a hash si coincide
     *
     * Devuelve true si la contraseña es válida.
     */
    private boolean verificarYMigrar(Empleado emp, String rawPassword) {
        String stored = emp.getContrasena();
        if (stored == null || stored.isBlank()) return false;

        // Hash BCrypt empieza con $2a$, $2b$ o $2y$ y mide 60 caracteres.
        if (stored.startsWith("$2") && stored.length() == 60) {
            return encoder.matches(rawPassword, stored);
        }

        // Texto plano legacy → si coincide, lo migramos a hash en este mismo login.
        if (stored.equals(rawPassword)) {
            emp.setContrasena(encoder.encode(rawPassword));
            empleadoRepo.save(emp);
            log.info("AuthService: contraseña de empleado {} migrada a BCrypt", emp.getIdEmpleado());
            return true;
        }
        return false;
    }
}
