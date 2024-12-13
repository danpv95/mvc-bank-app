package com.bankapp.mvc_bank_app.controller;


import com.bankapp.mvc_bank_app.model.Producto;
import com.bankapp.mvc_bank_app.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mvc/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody Producto producto) {
        Producto productoCreado = productoService.crearProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoCreado);
    }

    @PutMapping("/producto/{id}")
    public ResponseEntity<Producto> actualizarProductoPorId(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizarProducto(id, null, producto);
        return ResponseEntity.ok(productoActualizado);
    }

    @PutMapping("/cliente/producto/{idAccount}")
    public ResponseEntity<Producto> actualizarProductoPorNumeroCuenta(@PathVariable String idAccount, @Valid @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizarProducto(null, idAccount, producto);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProductoPorId(@PathVariable Long id) {
        productoService.eliminarProducto(id, null);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cliente/{idAccount}")
    public ResponseEntity<Void> eliminarProductoPorNumeroCuenta(@PathVariable String idAccount) {
        productoService.eliminarProducto(null, idAccount);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable String id) {
        Producto producto = productoService.obtenerProducto(id);
        return ResponseEntity.ok(producto);
    }
}
