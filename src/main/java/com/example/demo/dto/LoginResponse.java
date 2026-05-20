package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Integer idEmpleado;
    private String nombreEmpleado;
    private String correoEmpleado;
    private String curp;
    private String fotoPerfilUrl;
    private Integer idRol;
    private String nombreRol;
    private String descripcionRol;
}
