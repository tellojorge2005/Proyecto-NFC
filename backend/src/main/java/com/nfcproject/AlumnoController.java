package com.nfcproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.smartcardio.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AlumnoController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Endpoint para leer el UID desde el lector NFC
    @GetMapping("/api/leer-uid")
    public Map<String, String> leerNFC() {
        Map<String, String> response = new HashMap<>();
        try {
            // Inicializa el lector NFC
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            if (terminals.isEmpty()) {
                response.put("mensaje", "No se encontró un lector NFC");
                return response;
            }

            CardTerminal terminal = terminals.get(0);
            terminal.waitForCardPresent(0);
            Card card = terminal.connect("*");
            CardChannel channel = card.getBasicChannel();

            byte[] cmd = new byte[]{(byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            ResponseAPDU responseAPDU = channel.transmit(new CommandAPDU(cmd));
            byte[] uid = responseAPDU.getData();
            String uidHex = bytesToHex(uid);

            System.out.println("UID leído: " + uidHex);

            // Consulta en la base de datos para obtener información del alumno
            String sql = "SELECT nombre, apellidos, materia FROM alumno WHERE nfc = ?";
            Map<String, Object> alumnoData = jdbcTemplate.queryForMap(sql, uidHex);

            response.put("nfc", uidHex);
            response.put("nombre", (String) alumnoData.get("nombre"));
            response.put("apellidos", (String) alumnoData.get("apellidos"));
            response.put("materia", (String) alumnoData.get("materia"));

            card.disconnect(false);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje", "Error al leer el NFC o encontrar el alumno: " + e.getMessage());
        }
        return response;
    }
    @GetMapping("/api/leer-uidformulario")
    public Map<String, String> leerUID() {
        Map<String, String> response = new HashMap<>();

        try {
            // Lógica para leer el UID desde el lector NFC
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            if (terminals.isEmpty()) {
                response.put("mensaje", "No se encontró un lector NFC");
                return response;
            }

            CardTerminal terminal = terminals.get(0);
            terminal.waitForCardPresent(0);
            Card card = terminal.connect("*");
            CardChannel channel = card.getBasicChannel();

            byte[] cmd = new byte[]{(byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            ResponseAPDU responseAPDU = channel.transmit(new CommandAPDU(cmd));
            byte[] uid = responseAPDU.getData();
            String uidHex = bytesToHex(uid);

            response.put("uid", uidHex);
            response.put("mensaje", "UID leído con éxito");

            card.disconnect(false);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje", "Error al leer el UID");
        }

        return response;
    }

    // Endpoint para obtener la lista de estudiantes por materia
    @GetMapping("/api/lista-estudiantes")
    public List<Map<String, Object>> obtenerListaEstudiantes(@RequestParam("materia") String materia) {
        String sql = "SELECT nombre, apellidos, asistencia, nfc FROM alumno WHERE materia = ?";
        return jdbcTemplate.queryForList(sql, materia);
    }

    // Endpoint para marcar asistencia basado en el UID y la materia
    @PostMapping("/api/marcar-asistencia")
    public Map<String, String> marcarAsistencia(@RequestParam("nfc") String nfc, @RequestParam("materia") String materia) {
        Map<String, String> response = new HashMap<>();
        try {
            // Verifica si el alumno existe para la materia
            String sqlCheck = "SELECT COUNT(*) FROM alumno WHERE nfc = ? AND materia = ?";
            Integer count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, nfc, materia);

            if (count != null && count > 0) {
                // Marca la asistencia
                String sqlUpdate = "UPDATE alumno SET asistencia = 1 WHERE nfc = ? AND materia = ?";
                jdbcTemplate.update(sqlUpdate, nfc, materia);
                response.put("mensaje", "Asistencia marcada con éxito");
            } else {
                response.put("mensaje", "Alumno no encontrado para la materia especificada");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje", "Error al marcar la asistencia");
        }

        return response;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
