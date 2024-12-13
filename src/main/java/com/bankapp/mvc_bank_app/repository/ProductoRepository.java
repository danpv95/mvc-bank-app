package com.bankapp.mvc_bank_app.repository;

import com.bankapp.mvc_bank_app.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByNumeroCuenta(String numeroCuenta); // Cambiar a numeroCuenta
    Producto findTopByTipoCuentaOrderByNumeroCuentaDesc(String tipoCuenta);
    boolean existsByCliente_NumeroIdentificacion(String numeroIdentificacion);
    boolean existsByNumeroCuenta(String numeroCuenta);
}
