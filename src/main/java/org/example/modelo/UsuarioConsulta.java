package org.example.modelo;

public class UsuarioConsulta extends UsuarioSistema {
    public UsuarioConsulta() {
    }

    public UsuarioConsulta(int idUsuario, String nombreUsuario, String correoUsuario, boolean activo) {
        super(idUsuario, nombreUsuario, correoUsuario, activo);
    }

    @Override
    public int getIdRol() {
        return 2;
    }

    @Override
    public String getNombreRol() {
        return "Consulta";
    }

    @Override
    public String mostrarMenuPermitido() {
        return "Modulo de consulta pendiente";
    }

    @Override
    public boolean puedeAdministrarSolicitudes() {
        return false;
    }
}
