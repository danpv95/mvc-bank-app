package com.bankapp.mvc_bank_app.enums;

public enum TipoTransaccion {
    CONSIGNACION("Consignación"),
    RETIRO("Retiro"),
    TRANSFERENCIA("Transferencia");

    private final String nombre;

    TipoTransaccion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public static TipoTransaccion fromString(String tipo) {
        for (TipoTransaccion transaccion : TipoTransaccion.values()) {
            if (transaccion.nombre.equalsIgnoreCase(tipo.trim())) {
                return transaccion;
            }
        }
        throw new IllegalArgumentException("Tipo de transacción inválido: " + tipo);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
