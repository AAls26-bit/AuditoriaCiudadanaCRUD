package org.example.modelo;

import org.example.util.Validaciones;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Solicitud extends RegistroAuditoria {
    private String folioSolicitud;
    private String lugarSolicitud;
    private String asuntoSolicitud;
    private TipoSolicitud tipoSolicitud;
    private EstadoSolicitud estadoSolicitud;
    private int idUsuario;
    private TipoEntrega tipoEntrega;
    private LocalDate fechaEntrega;
    private LocalTime horaEntrega;
    private String areaRecepcion;
    private String cargoRecepcion;
    private String correoEntrega;
    private EvidenciaEntrega evidenciaEntrega;
    private LocalDate fechaVencimiento;

    public Solicitud() {
    }

    public Solicitud(String folioSolicitud,
                     LocalDate fechaFolio,
                     String lugarSolicitud,
                     String asuntoSolicitud,
                     TipoSolicitud tipoSolicitud,
                     EstadoSolicitud estadoSolicitud,
                     int idUsuario,
                     TipoEntrega tipoEntrega,
                     LocalDate fechaEntrega,
                     LocalTime horaEntrega,
                     String areaRecepcion,
                     String cargoRecepcion,
                     String correoEntrega,
                     EvidenciaEntrega evidenciaEntrega,
                     LocalDate fechaVencimiento) {
        super(fechaFolio);
        setFolioSolicitud(folioSolicitud);
        setLugarSolicitud(lugarSolicitud);
        setAsuntoSolicitud(asuntoSolicitud);
        setTipoSolicitud(tipoSolicitud);
        setEstadoSolicitud(estadoSolicitud);
        setIdUsuario(idUsuario);
        setTipoEntrega(tipoEntrega);
        setFechaEntrega(fechaEntrega);
        setHoraEntrega(horaEntrega);
        setAreaRecepcion(areaRecepcion);
        setCargoRecepcion(cargoRecepcion);
        setCorreoEntrega(correoEntrega);
        setEvidenciaEntrega(evidenciaEntrega);
        setFechaVencimiento(fechaVencimiento);
    }

    public String getFolioSolicitud() {
        return folioSolicitud;
    }

    public void setFolioSolicitud(String folioSolicitud) {
        String folio = Validaciones.textoObligatorio(folioSolicitud, "folio de solicitud", 45).toUpperCase(Locale.ROOT);
        this.folioSolicitud = folio;
    }

    public String getFechaFolio() {
        return getFechaRegistro();
    }

    public LocalDate getFechaFolioValor() {
        return getFechaRegistroValor();
    }

    public void setFechaFolio(LocalDate fechaFolio) {
        setFechaRegistro(fechaFolio);
    }

    public String getLugarSolicitud() {
        return Validaciones.capitalizar(lugarSolicitud);
    }

    public String getLugarSolicitudValor() {
        return lugarSolicitud;
    }

    public void setLugarSolicitud(String lugarSolicitud) {
        this.lugarSolicitud = Validaciones.textoObligatorio(lugarSolicitud, "lugar de solicitud", 60);
    }

    public String getAsuntoSolicitud() {
        return Validaciones.capitalizar(asuntoSolicitud);
    }

    public String getAsuntoSolicitudValor() {
        return asuntoSolicitud;
    }

    public void setAsuntoSolicitud(String asuntoSolicitud) {
        this.asuntoSolicitud = Validaciones.textoObligatorio(asuntoSolicitud, "asunto de solicitud", 255);
    }

    public String getTipoSolicitud() {
        return tipoSolicitud.getValorBD();
    }

    public TipoSolicitud getTipoSolicitudEnum() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
        this.tipoSolicitud = Validaciones.objetoObligatorio(tipoSolicitud, "tipo de solicitud");
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud.getValorBD();
    }

    public EstadoSolicitud getEstadoSolicitudEnum() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(EstadoSolicitud estadoSolicitud) {
        this.estadoSolicitud = Validaciones.objetoObligatorio(estadoSolicitud, "estado de solicitud");
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = Validaciones.enteroPositivo(idUsuario, "id de usuario");
    }

    public String getTipoEntrega() {
        return tipoEntrega.getValorBD();
    }

    public TipoEntrega getTipoEntregaEnum() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = Validaciones.objetoObligatorio(tipoEntrega, "tipo de entrega");
    }

    public String getFechaEntrega() {
        return Validaciones.fechaConFormato(fechaEntrega);
    }

    public LocalDate getFechaEntregaValor() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        LocalDate fechaValidada = Validaciones.fechaObligatoria(fechaEntrega, "fecha de entrega");
        if (fechaVencimiento != null && fechaValidada.isAfter(fechaVencimiento)) {
            throw new IllegalArgumentException("La fecha de entrega no puede ser posterior al vencimiento.");
        }
        this.fechaEntrega = fechaValidada;
    }

    public String getHoraEntrega() {
        return Validaciones.horaConFormato(horaEntrega);
    }

    public LocalTime getHoraEntregaValor() {
        return horaEntrega;
    }

    public void setHoraEntrega(LocalTime horaEntrega) {
        this.horaEntrega = Validaciones.horaObligatoria(horaEntrega, "hora de entrega");
    }

    public String getAreaRecepcion() {
        return Validaciones.capitalizar(areaRecepcion);
    }

    public String getAreaRecepcionValor() {
        return areaRecepcion;
    }

    public void setAreaRecepcion(String areaRecepcion) {
        this.areaRecepcion = Validaciones.textoObligatorio(areaRecepcion, "area de recepcion", 60);
    }

    public String getCargoRecepcion() {
        return Validaciones.capitalizar(cargoRecepcion);
    }

    public String getCargoRecepcionValor() {
        return cargoRecepcion;
    }

    public void setCargoRecepcion(String cargoRecepcion) {
        this.cargoRecepcion = Validaciones.textoObligatorio(cargoRecepcion, "cargo de recepcion", 60);
    }

    public String getCorreoEntrega() {
        return correoEntrega == null ? "Sin correo registrado" : correoEntrega;
    }

    public String getCorreoEntregaValor() {
        return correoEntrega;
    }

    public void setCorreoEntrega(String correoEntrega) {
        this.correoEntrega = Validaciones.correoOpcional(correoEntrega, "correo de entrega", 120);
    }

    public String getEvidenciaEntrega() {
        return evidenciaEntrega.getValorBD();
    }

    public EvidenciaEntrega getEvidenciaEntregaEnum() {
        return evidenciaEntrega;
    }

    public void setEvidenciaEntrega(EvidenciaEntrega evidenciaEntrega) {
        this.evidenciaEntrega = Validaciones.objetoObligatorio(evidenciaEntrega, "evidencia de entrega");
    }

    public String getFechaVencimiento() {
        return Validaciones.fechaConFormato(fechaVencimiento);
    }

    public LocalDate getFechaVencimientoValor() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        LocalDate fechaValidada = Validaciones.fechaObligatoria(fechaVencimiento, "fecha de vencimiento");
        if (fechaEntrega != null && fechaValidada.isBefore(fechaEntrega)) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la entrega.");
        }
        this.fechaVencimiento = fechaValidada;
    }

    public int getDiasParaVencimiento() {
        if (fechaVencimiento == null) {
            return 0;
        }
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
        if (dias > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (dias < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) dias;
    }

    public String getEstadoVencimiento() {
        int dias = getDiasParaVencimiento();
        if (dias > 0) {
            return "Faltan " + dias + " dias";
        }
        if (dias == 0) {
            return "Vence hoy";
        }
        return "Vencida hace " + Math.abs(dias) + " dias";
    }

    @Override
    public String getIdentificador() {
        return getFolioSolicitud();
    }

    @Override
    public String resumenCorto() {
        return getFolioSolicitud() + " | " + getTipoSolicitud() + " | " + getEstadoSolicitud();
    }

    @Override
    public boolean esValido() {
        return folioSolicitud != null
                && !folioSolicitud.isBlank()
                && getFechaFolioValor() != null
                && lugarSolicitud != null
                && !lugarSolicitud.isBlank()
                && asuntoSolicitud != null
                && !asuntoSolicitud.isBlank()
                && tipoSolicitud != null
                && estadoSolicitud != null
                && idUsuario > 0
                && tipoEntrega != null
                && fechaEntrega != null
                && horaEntrega != null
                && areaRecepcion != null
                && !areaRecepcion.isBlank()
                && cargoRecepcion != null
                && !cargoRecepcion.isBlank()
                && evidenciaEntrega != null
                && fechaVencimiento != null;
    }

    @Override
    public String toString() {
        return "=====================================\n"
                + "Folio: " + getFolioSolicitud() + "\n"
                + "Fecha de solicitud: " + getFechaFolio() + "\n"
                + "Lugar: " + getLugarSolicitud() + "\n"
                + "Asunto: " + getAsuntoSolicitud() + "\n"
                + "Tipo: " + getTipoSolicitud() + "\n"
                + "Estado: " + getEstadoSolicitud() + "\n"
                + "Id usuario: " + getIdUsuario() + "\n"
                + "Tipo de entrega: " + getTipoEntrega() + "\n"
                + "Fecha de entrega: " + getFechaEntrega() + "\n"
                + "Hora de entrega: " + getHoraEntrega() + "\n"
                + "Area de recepcion: " + getAreaRecepcion() + "\n"
                + "Cargo de recepcion: " + getCargoRecepcion() + "\n"
                + "Correo de entrega: " + getCorreoEntrega() + "\n"
                + "Evidencia: " + getEvidenciaEntrega() + "\n"
                + "Fecha de vencimiento: " + getFechaVencimiento() + "\n"
                + "Plazo: " + getEstadoVencimiento() + "\n"
                + "=====================================";
    }
}
