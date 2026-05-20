package com.example.demo.config;

import com.example.demo.entity.Empleado;
import com.example.demo.entity.Estado;
import com.example.demo.entity.Rol;
import com.example.demo.repository.EmpleadoRepository;
import com.example.demo.repository.EstadoRepository;
import com.example.demo.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Siembra catálogos básicos y un usuario administrador de prueba en el
 * primer arranque (idempotente: si ya existen, no hace nada).
 *
 * Para deshabilitar la siembra, definir la variable de entorno:
 *   APP_SEED_ENABLED=false
 *
 * Es seguro de ejecutar en cada boot — todas las operaciones son
 * condicionales sobre la existencia previa de los datos.
 *
 * Si la base de datos no está disponible al arranque, el seeder hace log y
 * sigue de largo: no aborta el contenedor.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class DataSeeder implements CommandLineRunner {

    private static final String SEED_ENV_FLAG = "APP_SEED_ENABLED";
    private static final String ADMIN_EMAIL   = "admin@indecsa.com";
    private static final String ADMIN_PASS    = "admin1234";

    private final RolRepository rolRepo;
    private final EstadoRepository estadoRepo;
    private final EmpleadoRepository empleadoRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if ("false".equalsIgnoreCase(System.getenv(SEED_ENV_FLAG))) {
            log.info("DataSeeder deshabilitado por {}=false", SEED_ENV_FLAG);
            return;
        }
        try {
            seedRoles();
            seedEstados();
            seedAdminEmpleado();
        } catch (Exception e) {
            log.warn("DataSeeder no pudo ejecutarse (posible BD no disponible): {}", e.getMessage());
        }
    }

    protected void seedRoles() {
        for (Rol.NombreRol nr : Rol.NombreRol.values()) {
            if (rolRepo.findByNombreRol(nr).isPresent()) continue;
            Rol r = new Rol();
            r.setNombreRol(nr);
            r.setDescripcionRol(nr == Rol.NombreRol.ADMIN
                    ? "Administrador del sistema"
                    : "Coordinación de capital humano");
            rolRepo.save(r);
            log.info("DataSeeder: Rol {} creado", nr);
        }
    }

    protected void seedEstados() {
        if (estadoRepo.count() > 0) return;
        for (String nombre : new String[]{
                "CDMX", "Estado de México", "Hidalgo", "Puebla", "Jalisco",
                "Nuevo León", "Querétaro", "Veracruz"
        }) {
            Estado e = new Estado();
            e.setNombreEst(nombre);
            estadoRepo.save(e);
        }
        log.info("DataSeeder: Estados sembrados");
    }

    protected void seedAdminEmpleado() {
        if (empleadoRepo.findByCorreoEmpleado(ADMIN_EMAIL).isPresent()) return;
        Rol admin = rolRepo.findByNombreRol(Rol.NombreRol.ADMIN).orElse(null);
        if (admin == null) {
            log.warn("DataSeeder: no se pudo crear admin porque no hay Rol ADMIN");
            return;
        }
        Empleado e = new Empleado();
        e.setNombreEmpleado("Administrador");
        e.setCurp("ADMI000000HDFXXX01");
        e.setCorreoEmpleado(ADMIN_EMAIL);
        e.setContrasena(encoder.encode(ADMIN_PASS));
        e.setRol(admin);
        empleadoRepo.save(e);
        log.info("DataSeeder: Empleado admin creado ({} / {})", ADMIN_EMAIL, ADMIN_PASS);
    }
}
