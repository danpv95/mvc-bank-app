package com.bankapp.mvc_bank_app.service;

import com.bankapp.mvc_bank_app.enums.TipoTransaccion;
import com.bankapp.mvc_bank_app.model.Producto;
import com.bankapp.mvc_bank_app.model.Transaccion;
import com.bankapp.mvc_bank_app.repository.ProductoRepository;
import com.bankapp.mvc_bank_app.repository.TransaccionRepository;
import com.bankapp.mvc_bank_app.utils.ValidacionesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ValidacionesUtils validacionesUtils;

    public Transaccion realizarTransaccion(Long productoIdOrigen, Long productoIdDestino,
                                           BigDecimal monto,
                                           TipoTransaccion tipo) {

        Producto productoOrigen = productoRepository.findByNumeroCuenta(String.valueOf(productoIdOrigen))
                .orElseThrow(() -> new RuntimeException("Producto origen no encontrado"));

        Producto productoDestino = null;

        if (tipo == TipoTransaccion.TRANSFERENCIA) {
            productoDestino = productoRepository.findById(productoIdDestino)
                    .orElseThrow(() -> new RuntimeException("Producto destino no encontrado"));
        }

        if (tipo == TipoTransaccion.RETIRO || tipo == TipoTransaccion.TRANSFERENCIA) {
            if (productoOrigen.getSaldo().compareTo(monto) < 0) {
                throw new RuntimeException("Saldo insuficiente para la transacción");
            }
        }

        Transaccion transaccion = new Transaccion();
        transaccion.setProducto(productoOrigen);
        transaccion.setMonto(monto);
        transaccion.setTipo(tipo);
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setSaldoAnterior(productoOrigen.getSaldo());

        if (tipo == TipoTransaccion.CONSIGNACION) {
            productoOrigen.setSaldo(productoOrigen.getSaldo().add(monto)); // Aumentar saldo
            transaccion.setSaldoNuevo(productoOrigen.getSaldo());
        } else if (tipo == TipoTransaccion.RETIRO) {
            productoOrigen.setSaldo(productoOrigen.getSaldo().subtract(monto)); // Disminuir saldo
            transaccion.setSaldoNuevo(productoOrigen.getSaldo());
        } else if (tipo == TipoTransaccion.TRANSFERENCIA) {

            productoOrigen.setSaldo(productoOrigen.getSaldo().subtract(monto)); // Disminuir saldo en origen
            transaccion.setSaldoNuevo(productoOrigen.getSaldo());

            productoDestino.setSaldo(productoDestino.getSaldo().add(monto)); // Aumentar saldo en destino

            Transaccion transaccionDestino = new Transaccion();
            transaccionDestino.setMonto(monto);
            transaccionDestino.setTipo(TipoTransaccion.TRANSFERENCIA);
            transaccionDestino.setFecha(LocalDateTime.now());
            transaccionDestino.setProducto(productoDestino);
            transaccionDestino.setSaldoAnterior(productoDestino.getSaldo().subtract(monto));
            transaccionDestino.setSaldoNuevo(productoDestino.getSaldo());
            transaccionRepository.save(transaccionDestino);
        }

        productoRepository.save(productoOrigen);

        if (productoDestino != null) {
            productoRepository.save(productoDestino);
        }

        return transaccionRepository.save(transaccion);
    }

    public List<Transaccion> obtenerTransaccionesPorProducto(Long productoId) {
        return transaccionRepository.findByProductoId(productoId);
    }
}
