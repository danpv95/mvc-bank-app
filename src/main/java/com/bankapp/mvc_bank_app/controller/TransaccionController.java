package com.bankapp.mvc_bank_app.controller;

import com.bankapp.mvc_bank_app.enums.TipoTransaccion;
import com.bankapp.mvc_bank_app.model.Transaccion;
import com.bankapp.mvc_bank_app.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/mvc/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @PostMapping("/realizar")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaccion realizarTransaccion(
            @RequestParam Long productoIdOrigen,
            @RequestParam(required = false) Long productoIdDestino,
            @RequestParam BigDecimal monto,
            @RequestParam String tipo) {
        TipoTransaccion tipoTransaccion = TipoTransaccion.valueOf(tipo.toUpperCase());  // Convertir a TipoTransaccion
        return transaccionService.realizarTransaccion(productoIdOrigen, productoIdDestino, monto, tipoTransaccion);
    }

    @GetMapping("/producto/{productoId}")
    public List<Transaccion> obtenerTransacciones(@PathVariable Long productoId) {
        return transaccionService.obtenerTransaccionesPorProducto(productoId);
    }
}
