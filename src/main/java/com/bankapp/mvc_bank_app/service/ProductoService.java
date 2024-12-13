package com.bankapp.mvc_bank_app.service;

import com.bankapp.mvc_bank_app.exception.ResourceNotFoundException;
import com.bankapp.mvc_bank_app.model.Producto;
import com.bankapp.mvc_bank_app.repository.ClienteRepository;
import com.bankapp.mvc_bank_app.repository.ProductoRepository;
import com.bankapp.mvc_bank_app.utils.Constants;
import com.bankapp.mvc_bank_app.utils.ValidacionesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.bankapp.mvc_bank_app.utils.Constants.*;
import static com.bankapp.mvc_bank_app.utils.ValidacionesUtils.isNull;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    private final ClienteRepository clienteRepository;

    private final ValidacionesUtils validacionesUtils;

    @Autowired
    public ProductoService(ProductoRepository productoRepository,
                           ClienteRepository clienteRepository,
                           ValidacionesUtils validacionesUtils) {
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.validacionesUtils = validacionesUtils;
    }

    public Producto crearProducto(Producto producto) {

        String tipoCuenta = producto.getTipoCuenta().toUpperCase();

        if (!tipoCuenta.equalsIgnoreCase(CUENTA_AHORROS) && !tipoCuenta.equalsIgnoreCase(CUENTA_CORRIENTE)) {
            throw new IllegalArgumentException("El tipo de cuenta debe ser 'Ahorros' o 'Corriente'");
        }

        if (producto.getCliente() == null || clienteRepository.findByNumeroIdentificacion(producto.getCliente().getNumeroIdentificacion()) == null) {
            throw new ResourceNotFoundException("El cliente asociado no existe");
        }

        if (producto.getSaldo() == null) {
            producto.setSaldo(BigDecimal.ZERO);
        }

        if (producto.getSaldo() != null && !ValidacionesUtils.saldoValido(producto)) {
            throw new IllegalArgumentException("El saldo no puede ser menor a 0");
        }

        Producto ultimaCuentaCreada = productoRepository.findTopByTipoCuentaOrderByNumeroCuentaDesc(tipoCuenta);
        ultimaCuentaCreada = isNull(String.valueOf(ultimaCuentaCreada))? null : ultimaCuentaCreada;

        String numeroCuenta = validacionesUtils.generarNumeroCuenta(tipoCuenta, ultimaCuentaCreada);
        producto.setNumeroCuenta(numeroCuenta);
        producto.setTipoCuenta(tipoCuenta);
        producto.setEstado(tipoCuenta.equals(CUENTA_AHORROS)? ACTIVA : producto.getEstado());
        validacionesUtils.actualizarFechaCreacion(producto);

        producto.setCliente(clienteRepository.findByNumeroIdentificacion(producto.getCliente().getNumeroIdentificacion()).get());

        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, String numeroCuenta, Producto productoActualizado) {
        Producto producto;

        if (numeroCuenta != null) {
            producto = productoRepository.findByNumeroCuenta(numeroCuenta)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        } else {
            producto = productoRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        }

        if (CANCELADA.equals(productoActualizado.getEstado())){
            if (producto.getSaldo().compareTo(BigDecimal.ZERO) == 0){
                producto.setEstado(CANCELADA);
            }else {
                throw new IllegalArgumentException("El estado solo puede cambiar a CANCELADA si el saldo es 0");
            }
        }

        producto.setTipoCuenta(!isNull(productoActualizado.getTipoCuenta()) ?
                productoActualizado.getTipoCuenta() : producto.getTipoCuenta());

        producto.setEstado(!isNull(productoActualizado.getEstado()) ?
                productoActualizado.getEstado().toUpperCase() : producto.getEstado());

        producto.setExentaGMF(!isNull(String.valueOf(productoActualizado.getExentaGMF())) ?
                productoActualizado.getExentaGMF() : producto.getExentaGMF());


        ValidacionesUtils.actualizarFechaModificacion(producto);

        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id, String numeroCuenta) {
        Producto producto;

        if (numeroCuenta != null) {
            producto = productoRepository.findByNumeroCuenta(numeroCuenta)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        } else {
            producto = productoRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        }

        if (producto.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("No se puede eliminar el producto con saldo positivo");
        }

        productoRepository.deleteById(id);
    }

    public Producto obtenerProducto(String id) {

        Producto producto = productoRepository.findById(Long.parseLong(id))
                .orElse(null);

        if (producto == null) {
            producto = productoRepository.findByNumeroCuenta(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        }

        return producto;
    }
}
