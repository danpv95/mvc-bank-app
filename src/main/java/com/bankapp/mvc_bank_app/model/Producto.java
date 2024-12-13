package com.bankapp.mvc_bank_app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String tipoCuenta;  //  Cuenta de Ahorro o Cuenta Corriente

    @Column(unique = true)
    private String numeroCuenta;

    private String estado;  //  "activa", "inactiva" ó "cancelada"

    @NotNull
    private BigDecimal saldo;

    @NotNull
    private Boolean exentaGMF;

    private LocalDate fechaCreacion;
    private LocalDate fechaModificacion;

    @ManyToOne
    private Cliente cliente;

}
