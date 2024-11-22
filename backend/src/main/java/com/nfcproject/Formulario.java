package com.nfcproject;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Formulario {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("api/registrar-alumno")
    public Map<String, String> registrarAlumno(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("grado") String grado_academico,
            @RequestParam("semestre") String semestre,
            @RequestParam("matricula") String matricula,
            @RequestParam("telefono") String telefono,
            @RequestParam("correo") String correo,
            @RequestParam("contraseña") String contraseña,
            @RequestParam("materia") String materia,
            @RequestParam("campus") String campus,
            @RequestParam("nfc") String nfc,
            @RequestParam("foto") MultipartFile fotografia) {

        Map<String, String> response = new HashMap<>();

        try {
            // Recupera el tipo MIME del archivo subido
            String mimeType = fotografia.getContentType();

            // SQL para insertar datos en la tabla
            String sql = "INSERT INTO alumno (nombre, apellidos, grado_academico, semestre, matricula, telefono, correo, contraseña, nfc, fotografia, materia, campus, mime_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Inserta datos en la base de datos
            jdbcTemplate.update(sql, nombre, apellidos, grado_academico, semestre, matricula, telefono, correo, contraseña, nfc, fotografia.getBytes(), materia, campus, mimeType);

            response.put("mensaje", "Alumno registrado con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("mensaje", "Error al registrar al alumno: " + e.getMessage());
        }

        return response;
    }
}
