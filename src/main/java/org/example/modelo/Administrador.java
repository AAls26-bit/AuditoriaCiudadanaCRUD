package org.example.modelo;

public class Administrador extends UsuarioSistema {
    public Administrador() {
    }

    public Administrador(int idUsuario, String nombreUsuario, String correoUsuario, boolean activo) {
        super(idUsuario, nombreUsuario, correoUsuario, activo);
    }

    @Override
    public int getIdRol() {
        return 1;
    }

    @Override
    public String getNombreRol() {
        return "Administrador";
    }

    @Override
    public String mostrarMenuPermitido() {
        return "CRUD de solicitudes";
    }

    @Override
    public boolean puedeAdministrarSolicitudes() {
        return true;
    }
}
