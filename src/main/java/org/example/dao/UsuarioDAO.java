package org.example.dao;

import org.example.config.Conexion;
import org.example.modelo.UsuarioSistema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioDAO {
    public UsuarioSistema obtenerOCrearUsuarioDemo(UsuarioSistema usuario, String passwordHash) {
        String hash = passwordHash == null || passwordHash.isBlank() ? "demo_hash" : passwordHash.trim();

        try (Connection conexion = Conexion.conectar()) {
            Integer idEncontrado = buscarIdPorCorreo(conexion, usuario.getCorreoUsuario());
            if (idEncontrado != null) {
                usuario.setIdUsuario(idEncontrado);
                return usuario;
            }

            String sql = "INSERT INTO Usuario(nombreUsuario, correoUsuario, passwordHash, activo, idRol) "
                    + "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stm = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stm.setString(1, usuario.getNombreUsuarioValor());
                stm.setString(2, usuario.getCorreoUsuario());
                stm.setString(3, hash);
                stm.setBoolean(4, usuario.isActivoValor());
                stm.setInt(5, usuario.getIdRol());
                stm.executeUpdate();

                try (ResultSet rs = stm.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setIdUsuario(rs.getInt(1));
                    }
                }
            }
        } catch (SQLException err) {
            System.out.println("No se pudo preparar el usuario demo: " + err.getMessage());
        }

        return usuario;
    }

    private Integer buscarIdPorCorreo(Connection conexion, String correo) throws SQLException {
        String sql = "SELECT idUsuario FROM Usuario WHERE correoUsuario = ?";
        try (PreparedStatement stm = conexion.prepareStatement(sql)) {
            stm.setString(1, correo);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idUsuario");
                }
            }
        }
        return null;
    }
}
