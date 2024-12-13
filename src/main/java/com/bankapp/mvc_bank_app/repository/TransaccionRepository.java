package com.bankapp.mvc_bank_app.repository;

import com.bankapp.mvc_bank_app.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByProductoId(Long productoId);
}
