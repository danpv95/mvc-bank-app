package com.bankapp.mvc_bank_app.service;

import com.bankapp.mvc_bank_app.exception.ResourceNotFoundException;
import com.bankapp.mvc_bank_app.model.Cliente;
import com.bankapp.mvc_bank_app.repository.ClienteRepository;
import com.bankapp.mvc_bank_app.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bankapp.mvc_bank_app.utils.ValidacionesUtils.*;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    private final ProductoRepository productoRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,
                          ProductoRepository productoRepository) {
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    public Cliente crearCliente(Cliente cliente) {

        if (clienteRepository.findByNumeroIdentificacion(cliente.getNumeroIdentificacion()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cliente con ese número de identificación.");
        }

        if (!esMayorDeEdad(cliente)) {
            throw new IllegalArgumentException("El cliente debe ser mayor de edad.");
        }

        actualizarFechaCreacion(cliente);  // Establecer fecha de creación

        return clienteRepository.save(cliente);
    }


    public Cliente actualizarCliente(String id, Cliente clienteActualizado) {

        Optional<Cliente> clienteExistente = clienteRepository.findByNumeroIdentificacion(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();

            if (!cliente.getNumeroIdentificacion().equals(clienteActualizado.getNumeroIdentificacion())) {

                if (clienteRepository.findByNumeroIdentificacion(clienteActualizado.getNumeroIdentificacion()).isPresent()) {
                    throw new IllegalArgumentException("Ya existe un cliente con ese número de identificación.");
                }
            }

            actualizarFechaModificacion(cliente);

            cliente.setNombres(clienteActualizado.getNombres());
            cliente.setApellidos(clienteActualizado.getApellidos());
            cliente.setCorreoElectronico(clienteActualizado.getCorreoElectronico());
            cliente.setFechaNacimiento(clienteActualizado.getFechaNacimiento());

            return clienteRepository.save(cliente);
        } else {
            throw new ResourceNotFoundException("Cliente no encontrado.");
        }
    }


    public void eliminarCliente(String id) {
        Optional<Cliente> cliente = clienteRepository.findByNumeroIdentificacion(id);
        if (cliente.isPresent()) {
            if (productoRepository.existsByCliente_NumeroIdentificacion(id)) {
                throw new IllegalArgumentException("No se puede eliminar un cliente con productos vinculados.");
            }
            clienteRepository.deleteById(cliente.get().getId());
        } else {
            throw new ResourceNotFoundException("Cliente no encontrado.");
        }
    }

    public Cliente obtenerCliente(String id) {

        return clienteRepository.findByNumeroIdentificacion(id).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado."));

    }
}
