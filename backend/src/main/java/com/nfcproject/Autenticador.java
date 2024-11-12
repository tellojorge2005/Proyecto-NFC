package com.nfcproject;

import java.sql.*;
import org.springframework.stereotype.Component;

@Component
public class Autenticador {

    public String authenticateUser(String usuario, String contra) {
        String rol = "invalido"; // Valor predeterminado si la autenticación falla
        String url = "jdbc:mysql://database-nfc.cnoacewu42c5.us-east-2.rds.amazonaws.com:3306/db_nfc";
        String username = "admin";
        String password = "PAssWorD.123";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(url, username, password);
            String query = "SELECT grado_academico FROM alumno WHERE correo = ? AND contraseña = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, usuario);
            statement.setString(2, contra);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                rol = resultSet.getString("grado_academico"); // Obtener el rol del usuario
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rol;
    }
}
