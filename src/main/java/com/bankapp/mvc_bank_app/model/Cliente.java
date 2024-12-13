package com.bankapp.mvc_bank_app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String tipoIdentificacion;

    @Column(unique = true, name = "numero_identificacion")
    @NotEmpty(message = "Debe haber un numero de identificacion")
    private String numeroIdentificacion;

    @NotEmpty
    @Size(min = 2)
    private String nombres;

    @NotEmpty
    @Size(min = 2)
    private String apellidos;

    @NotEmpty
    @Email
    private String correoElectronico;

    private LocalDate fechaNacimiento;
    private LocalDate fechaCreacion;
    private LocalDate fechaModificacion;
}
