package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Domicilio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Domicilio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_domicilio")
    private Integer idDomicilio;

    @Column(name = "calle", nullable = false, length = 100)
    private String calle;

    @Column(name = "num_ext", nullable = false, length = 10)
    private String numExt;

    @Column(name = "num_int", length = 10)
    private String numInt;

    @Column(name = "colonia", nullable = false, length = 100)
    private String colonia;

    @Column(name = "cod_post", nullable = false)
    private Integer codPost;

    @Column(name = "mun_alc", nullable = false, length = 100)
    private String munAlc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;
}
