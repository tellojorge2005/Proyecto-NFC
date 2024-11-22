package com.nfcproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AlumnoController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Endpoint para obtener la lista de estudiantes por materia
    @GetMapping("/api/lista-estudiantes")
    public List<Map<String, Object>> obtenerListaEstudiantes(@RequestParam("materia") String materia) {
        String sql = "SELECT nombre, apellidos, asistencia FROM alumno WHERE materia = ?";
        return jdbcTemplate.queryForList(sql, materia);
    }

    // Nuevo endpoint para marcar asistencia basado en el UID y la materia
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
}
