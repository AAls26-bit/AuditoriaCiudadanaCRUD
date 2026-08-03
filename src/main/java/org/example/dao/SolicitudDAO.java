package org.example.dao;

import org.example.config.Conexion;
import org.example.modelo.EstadoSolicitud;
import org.example.modelo.EvidenciaEntrega;
import org.example.modelo.Solicitud;
import org.example.modelo.TipoEntrega;
import org.example.modelo.TipoSolicitud;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Locale;

public class SolicitudDAO implements OperacionesCrud<Solicitud, String> {
    private static final String SELECT_BASE = "SELECT "
            + "s.folioSolicitud, s.fechaFolio, s.lugarSolicitud, s.asuntoSolicitud, "
            + "s.tipoSolicitud, s.estadoSolicitud, s.idUsuario, "
            + "e.tipoEntrega, e.fechaEntrega, e.horaEntrega, e.areaRecepcion, "
            + "e.cargoRecepcion, e.correoEntrega, e.evidenciaEntrega, e.fechaVencimiento "
            + "FROM Solicitud s "
            + "INNER JOIN Entrega e ON s.folioSolicitud = e.folioSolicitud ";

    @Override
    public boolean agregar(Solicitud solicitud) {
        if (solicitud == null || !solicitud.esValido()) {
            System.out.println("La solicitud no tiene datos validos.");
            return false;
        }

        String sqlSolicitud = "INSERT INTO Solicitud("
                + "folioSolicitud, fechaFolio, lugarSolicitud, asuntoSolicitud, tipoSolicitud, estadoSolicitud, idUsuario"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlEntrega = "INSERT INTO Entrega("
                + "folioSolicitud, tipoEntrega, fechaEntrega, horaEntrega, areaRecepcion, cargoRecepcion, "
                + "correoEntrega, evidenciaEntrega, fechaVencimiento"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar()) {
            conexion.setAutoCommit(false);

            try (PreparedStatement stmSolicitud = conexion.prepareStatement(sqlSolicitud);
                 PreparedStatement stmEntrega = conexion.prepareStatement(sqlEntrega)) {

                llenarSolicitudInsert(stmSolicitud, solicitud);
                llenarEntregaInsert(stmEntrega, solicitud);

                stmSolicitud.executeUpdate();
                stmEntrega.executeUpdate();
                conexion.commit();
                System.out.println("Solicitud registrada correctamente.");
                return true;
            } catch (SQLException err) {
                conexion.rollback();
                throw err;
            }
        } catch (SQLException err) {
            System.out.println("Error al registrar la solicitud: " + err.getMessage());
            return false;
        }
    }

    @Override
    public boolean editar(Solicitud solicitud) {
        if (solicitud == null || !solicitud.esValido()) {
            System.out.println("La solicitud no tiene datos validos.");
            return false;
        }

        String sqlSolicitud = "UPDATE Solicitud SET "
                + "fechaFolio = ?, lugarSolicitud = ?, asuntoSolicitud = ?, tipoSolicitud = ?, "
                + "estadoSolicitud = ?, idUsuario = ? "
                + "WHERE folioSolicitud = ?";
        String sqlEntrega = "UPDATE Entrega SET "
                + "tipoEntrega = ?, fechaEntrega = ?, horaEntrega = ?, areaRecepcion = ?, "
                + "cargoRecepcion = ?, correoEntrega = ?, evidenciaEntrega = ?, fechaVencimiento = ? "
                + "WHERE folioSolicitud = ?";

        try (Connection conexion = Conexion.conectar()) {
            conexion.setAutoCommit(false);

            try (PreparedStatement stmSolicitud = conexion.prepareStatement(sqlSolicitud);
                 PreparedStatement stmEntrega = conexion.prepareStatement(sqlEntrega)) {

                llenarSolicitudUpdate(stmSolicitud, solicitud);
                int filasSolicitud = stmSolicitud.executeUpdate();
                if (filasSolicitud == 0) {
                    conexion.rollback();
                    System.out.println("No existe una solicitud con ese folio.");
                    return false;
                }

                llenarEntregaUpdate(stmEntrega, solicitud);
                int filasEntrega = stmEntrega.executeUpdate();
                if (filasEntrega == 0) {
                    conexion.rollback();
                    System.out.println("La solicitud existe, pero no tiene registro de entrega asociado.");
                    return false;
                }

                conexion.commit();
                System.out.println("Solicitud actualizada correctamente.");
                return true;
            } catch (SQLException err) {
                conexion.rollback();
                throw err;
            }
        } catch (SQLException err) {
            System.out.println("Error al actualizar la solicitud: " + err.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(String folioSolicitud) {
        String folio = normalizarFolio(folioSolicitud);
        if (folio.isEmpty()) {
            System.out.println("El folio es obligatorio.");
            return false;
        }

        String sql = "DELETE FROM Solicitud WHERE folioSolicitud = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement stm = conexion.prepareStatement(sql)) {
            stm.setString(1, folio);
            int filas = stm.executeUpdate();

            if (filas > 0) {
                System.out.println("Solicitud eliminada correctamente.");
                return true;
            }

            System.out.println("No se encontro una solicitud con ese folio.");
            return false;
        } catch (SQLException err) {
            System.out.println("Error al eliminar la solicitud: " + err.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Solicitud> listar() {
        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY s.fechaFolio DESC, s.folioSolicitud ASC";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement stm = conexion.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                solicitudes.add(mapearSolicitud(rs));
            }
        } catch (SQLException err) {
            System.out.println("Error al listar solicitudes: " + err.getMessage());
        }

        return solicitudes;
    }

    @Override
    public Solicitud buscarPorId(String folioSolicitud) {
        String folio = normalizarFolio(folioSolicitud);
        if (folio.isEmpty()) {
            return null;
        }

        String sql = SELECT_BASE + "WHERE s.folioSolicitud = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement stm = conexion.prepareStatement(sql)) {
            stm.setString(1, folio);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapearSolicitud(rs);
                }
            }
        } catch (SQLException err) {
            System.out.println("Error al buscar solicitud: " + err.getMessage());
        }

        return null;
    }

    @Override
    public ArrayList<Solicitud> filtrar(String texto) {
        if (texto == null || texto.isBlank()) {
            return listar();
        }

        ArrayList<Solicitud> solicitudes = new ArrayList<>();
        String filtro = "%" + texto.trim() + "%";
        String sql = SELECT_BASE
                + "WHERE s.folioSolicitud LIKE ? "
                + "OR s.lugarSolicitud LIKE ? "
                + "OR s.asuntoSolicitud LIKE ? "
                + "OR s.tipoSolicitud LIKE ? "
                + "OR s.estadoSolicitud LIKE ? "
                + "OR e.tipoEntrega LIKE ? "
                + "OR e.areaRecepcion LIKE ? "
                + "OR e.evidenciaEntrega LIKE ? "
                + "ORDER BY s.fechaFolio DESC, s.folioSolicitud ASC";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement stm = conexion.prepareStatement(sql)) {
            for (int i = 1; i <= 8; i++) {
                stm.setString(i, filtro);
            }

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    solicitudes.add(mapearSolicitud(rs));
                }
            }
        } catch (SQLException err) {
            System.out.println("Error al filtrar solicitudes: " + err.getMessage());
        }

        return solicitudes;
    }

    private void llenarSolicitudInsert(PreparedStatement stm, Solicitud solicitud) throws SQLException {
        stm.setString(1, solicitud.getFolioSolicitud());
        stm.setDate(2, Date.valueOf(solicitud.getFechaFolioValor()));
        stm.setString(3, solicitud.getLugarSolicitudValor());
        stm.setString(4, solicitud.getAsuntoSolicitudValor());
        stm.setString(5, solicitud.getTipoSolicitud());
        stm.setString(6, solicitud.getEstadoSolicitud());
        stm.setInt(7, solicitud.getIdUsuario());
    }

    private void llenarSolicitudUpdate(PreparedStatement stm, Solicitud solicitud) throws SQLException {
        stm.setDate(1, Date.valueOf(solicitud.getFechaFolioValor()));
        stm.setString(2, solicitud.getLugarSolicitudValor());
        stm.setString(3, solicitud.getAsuntoSolicitudValor());
        stm.setString(4, solicitud.getTipoSolicitud());
        stm.setString(5, solicitud.getEstadoSolicitud());
        stm.setInt(6, solicitud.getIdUsuario());
        stm.setString(7, solicitud.getFolioSolicitud());
    }

    private void llenarEntregaInsert(PreparedStatement stm, Solicitud solicitud) throws SQLException {
        stm.setString(1, solicitud.getFolioSolicitud());
        stm.setString(2, solicitud.getTipoEntrega());
        stm.setDate(3, Date.valueOf(solicitud.getFechaEntregaValor()));
        stm.setTime(4, Time.valueOf(solicitud.getHoraEntregaValor()));
        stm.setString(5, solicitud.getAreaRecepcionValor());
        stm.setString(6, solicitud.getCargoRecepcionValor());
        stm.setString(7, solicitud.getCorreoEntregaValor());
        stm.setString(8, solicitud.getEvidenciaEntrega());
        stm.setDate(9, Date.valueOf(solicitud.getFechaVencimientoValor()));
    }

    private void llenarEntregaUpdate(PreparedStatement stm, Solicitud solicitud) throws SQLException {
        stm.setString(1, solicitud.getTipoEntrega());
        stm.setDate(2, Date.valueOf(solicitud.getFechaEntregaValor()));
        stm.setTime(3, Time.valueOf(solicitud.getHoraEntregaValor()));
        stm.setString(4, solicitud.getAreaRecepcionValor());
        stm.setString(5, solicitud.getCargoRecepcionValor());
        stm.setString(6, solicitud.getCorreoEntregaValor());
        stm.setString(7, solicitud.getEvidenciaEntrega());
        stm.setDate(8, Date.valueOf(solicitud.getFechaVencimientoValor()));
        stm.setString(9, solicitud.getFolioSolicitud());
    }

    private Solicitud mapearSolicitud(ResultSet rs) throws SQLException {
        return new Solicitud(
                rs.getString("folioSolicitud"),
                rs.getDate("fechaFolio").toLocalDate(),
                rs.getString("lugarSolicitud"),
                rs.getString("asuntoSolicitud"),
                TipoSolicitud.desdeValorBD(rs.getString("tipoSolicitud")),
                EstadoSolicitud.desdeValorBD(rs.getString("estadoSolicitud")),
                rs.getInt("idUsuario"),
                TipoEntrega.desdeValorBD(rs.getString("tipoEntrega")),
                rs.getDate("fechaEntrega").toLocalDate(),
                rs.getTime("horaEntrega").toLocalTime(),
                rs.getString("areaRecepcion"),
                rs.getString("cargoRecepcion"),
                rs.getString("correoEntrega"),
                EvidenciaEntrega.desdeValorBD(rs.getString("evidenciaEntrega")),
                rs.getDate("fechaVencimiento").toLocalDate()
        );
    }

    private String normalizarFolio(String folioSolicitud) {
        if (folioSolicitud == null) {
            return "";
        }
        return folioSolicitud.trim().toUpperCase(Locale.ROOT);
    }
}
