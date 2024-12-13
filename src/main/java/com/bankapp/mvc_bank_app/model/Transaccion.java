package com.bankapp.mvc_bank_app.model;

import com.bankapp.mvc_bank_app.enums.TipoTransaccion;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private BigDecimal monto;

    private TipoTransaccion tipo;       // "DEPOSITO", "RETIRO" ó "TRANSFERENCIA"

    private LocalDateTime fecha;

    private BigDecimal saldoAnterior;

    private BigDecimal saldoNuevo;
}
