package com.nfcproject;

import javax.smartcardio.*;
import java.sql.*;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;

@SpringBootApplication
@RestController
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/api/leer-nfc")
    public String leerNFC() {
        String url = "jdbc:mysql://database-nfc.cnoacewu42c5.us-east-2.rds.amazonaws.com:3306/db_nfc";
        String username = "admin";
        String password = "PAssWorD.123";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Conexión exitosa a MySQL!");

            // Leer UID desde el lector NFC
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            if (terminals.isEmpty()) {
                return "{\"mensaje\": \"No se encontró un lector NFC\"}";
            }

            CardTerminal terminal = terminals.get(0);
            terminal.waitForCardPresent(0);
            Card card = terminal.connect("*");
            CardChannel channel = card.getBasicChannel();

            byte[] cmd = new byte[]{(byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            ResponseAPDU response = channel.transmit(new CommandAPDU(cmd));
            byte[] uid = response.getData();
            String uidHex = bytesToHex(uid);

            System.out.println("UID leído: " + uidHex);

            // Consulta SQL para buscar el UID en la base de datos
            statement = connection.createStatement();
            String query = "SELECT nombre, apellidos, matricula, fotografia, grado_academico, campus FROM alumno WHERE nfc = '" + uidHex + "'";
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                // Si se encuentra, devuelve los datos del alumno en formato JSON
                String nombre = resultSet.getString("nombre");
                String apellidos = resultSet.getString("apellidos");
                String matricula = resultSet.getString("matricula");
                String fotografia = resultSet.getString("fotografia");
                String gradoAcademico = resultSet.getString("grado_academico");
                String campus = resultSet.getString("campus");

                card.disconnect(false);

                return String.format(
                        "{\"nombre\": \"%s\", \"apellidos\": \"%s\", \"matricula\": \"%s\", \"fotografia\": \"%s\", \"grado_academico\": \"%s\", \"campus\": \"%s\"}",
                        nombre, apellidos, matricula, fotografia, gradoAcademico, campus);
            } else {
                // Si no se encuentra el UID, devolver "Acceso denegado"
                card.disconnect(false);
                return "{\"mensaje\": \"Acceso denegado\"}";
            }

        } catch (SQLException | CardException e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Autowired
    private Autenticador autenticador;

    @PostMapping("/api/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) {
        String correo = credentials.get("correo");
        String contra = credentials.get("contraseña");

        String rol = autenticador.authenticateUser(correo, contra);

        Map<String, String> response = new HashMap<>();
        if (!"invalido".equals(rol)) {
            response.put("mensaje", "Autenticación exitosa");
            response.put("rol", rol); // Devolver el rol del usuario
        } else {
            response.put("mensaje", "Credenciales incorrectas");
        }
        return response;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}