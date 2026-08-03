package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexion {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/auditoriaciudadana"
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=America/Mexico_City";
    private static final String USER = "root";
    private static final String PASSWORD = "26112007@S";

    private Conexion() {
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
