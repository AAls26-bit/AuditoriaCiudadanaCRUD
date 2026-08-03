package org.example.modelo;

import org.example.util.Validaciones;

public abstract class UsuarioSistema implements Validable, PermisosSolicitud {
    private int idUsuario;
    private String nombreUsuario;
    private String correoUsuario;
    private boolean activo;

    public UsuarioSistema() {
    }

    public UsuarioSistema(int idUsuario, String nombreUsuario, String correoUsuario, boolean activo) {
        setIdUsuario(idUsuario);
        setNombreUsuario(nombreUsuario);
        setCorreoUsuario(correoUsuario);
        setActivo(activo);
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = Validaciones.enteroCeroOPositivo(idUsuario, "id de usuario");
    }

    public String getNombreUsuario() {
        return Validaciones.capitalizar(nombreUsuario);
    }

    public String getNombreUsuarioValor() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = Validaciones.textoObligatorio(nombreUsuario, "nombre de usuario", 100);
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = Validaciones.correoObligatorio(correoUsuario, "correo de usuario", 120);
    }

    public String getActivo() {
        return activo ? "Activo" : "Inactivo";
    }

    public boolean isActivoValor() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public abstract int getIdRol();

    public abstract String getNombreRol();

    public abstract String mostrarMenuPermitido();

    @Override
    public boolean esValido() {
        return idUsuario >= 0
                && nombreUsuario != null
                && !nombreUsuario.isBlank()
                && correoUsuario != null
                && !correoUsuario.isBlank();
    }

    @Override
    public String toString() {
        return "Id usuario: " + getIdUsuario() + "\n"
                + "Nombre: " + getNombreUsuario() + "\n"
                + "Correo: " + getCorreoUsuario() + "\n"
                + "Rol: " + getNombreRol() + "\n"
                + "Estatus: " + getActivo() + "\n"
                + "Menu permitido: " + mostrarMenuPermitido();
    }
}
