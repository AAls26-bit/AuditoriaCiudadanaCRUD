package org.example.modelo;

import org.example.util.Validaciones;

import java.time.LocalDate;

public abstract class RegistroAuditoria implements Validable {
    private LocalDate fechaRegistro;

    public RegistroAuditoria() {
    }

    public RegistroAuditoria(LocalDate fechaRegistro) {
        setFechaRegistro(fechaRegistro);
    }

    public String getFechaRegistro() {
        return Validaciones.fechaConFormato(fechaRegistro);
    }

    public LocalDate getFechaRegistroValor() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = Validaciones.fechaObligatoria(fechaRegistro, "fecha de registro");
    }

    public abstract String getIdentificador();

    public abstract String resumenCorto();
}
