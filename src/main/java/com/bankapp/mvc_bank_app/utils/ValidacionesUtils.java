package com.bankapp.mvc_bank_app.utils;

import com.bankapp.mvc_bank_app.model.Cliente;
import com.bankapp.mvc_bank_app.model.Producto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Component
public class ValidacionesUtils {


    public static boolean saldoValido(Producto producto) {
        return producto.getSaldo().compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean esMayorDeEdad(Cliente cliente) {
        if (cliente.getFechaNacimiento() == null) {
            return false;
        }
        Period edad = Period.between(cliente.getFechaNacimiento(), LocalDate.now());
        return edad.getYears() >= 18;  // El cliente debe tener 18 o más años
    }

    public static boolean isNull(String str) {
        return str == null || str.trim().isEmpty();
    }

    public String generarNumeroCuenta(String tipoCuenta, Producto ultimaCuenta) {

        String prefijo = tipoCuenta.equalsIgnoreCase(Constants.CUENTA_AHORROS) ? "53" : "33";

        String ultimoNumeroCuenta = ultimaCuenta == null ? null : ultimaCuenta.getNumeroCuenta();

        if (isNull(ultimoNumeroCuenta)) {
            return prefijo + String.format("%08d", 1);
        }

        String numeroCuenta = ultimoNumeroCuenta.substring(prefijo.length());
        int nuevoNumeroCuenta = Integer.parseInt(numeroCuenta) + 1;

        return prefijo + String.format("%08d", nuevoNumeroCuenta);
    }

    public static void actualizarFechaCreacion(Cliente cliente) {
        if (cliente.getFechaCreacion() == null) {
            cliente.setFechaCreacion(LocalDate.now());
        }
        if (cliente.getFechaModificacion() == null) {
            actualizarFechaModificacion(cliente);
        }
    }

    public static void actualizarFechaCreacion(Producto producto) {
        if (producto.getFechaCreacion() == null) {
            producto.setFechaCreacion(LocalDate.now());
        }
        if (producto.getFechaModificacion() == null) {
            actualizarFechaModificacion(producto);
        }
    }

    public static void actualizarFechaModificacion(Cliente cliente) {
        cliente.setFechaModificacion(LocalDate.now());
    }

    public static void actualizarFechaModificacion(Producto producto) {
        producto.setFechaModificacion(LocalDate.now());
    }
}
