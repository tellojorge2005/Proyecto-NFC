# Sistema Integral de Control de Acceso a Campus

## Descripción
Este proyecto permite gestionar la asistencia de estudiantes mediante el uso de tarjetas NFC, ofreciendo una interfaz web interactiva y una API robusta que conecta con una base de datos MySQL.

## Funcionalidades principales
1. **Registro de estudiantes**:
   - Formulario web para registrar estudiantes con campos como nombre, apellidos, matrícula, NFC, materia, campus, entre otros.
   - Subida de foto del estudiante.

2. **Lectura de NFC**:
   - Detecta el UID de las tarjetas NFC en tiempo real.
   - Autocompleta el campo NFC en el formulario al detectar una tarjeta.

3. **Gestión de asistencia**:
   - Lista interactiva de estudiantes por materia.
   - Marca asistencia automáticamente al detectar una tarjeta NFC válida.

4. **Dashboard de administración**:
   - Visualización y gestión de estudiantes y asistencia.

## Tecnología utilizada
- **Frontend**:
  - HTML5, CSS3 y JavaScript.
  - Diseño interactivo con estilos personalizados.

- **Backend**:
  - Spring Boot para la API.
  - Lógica de lectura de tarjetas NFC utilizando `javax.smartcardio`.

- **Base de datos**:
  - MySQL para almacenar estudiantes, materias y registros de asistencia.

## Cómo usar
1. **Configuración del entorno**:
   - Configura una base de datos MySQL y actualiza el archivo `application.properties` con las credenciales correctas.
   - Crea las tablas necesarias siguiendo el modelo definido en el proyecto.

2. **Ejecutar el proyecto**:
   - Usa un IDE como IntelliJ para ejecutar el backend.
   - Sirve los archivos HTML y JavaScript en un servidor local.

3. **Pruebas**:
   - Registra un estudiante desde el formulario.
   - Usa una tarjeta NFC para validar asistencia en tiempo real.

## Contribuidores
- Kevin Rucoba Moreno: Desarrollo e implentación del backend y frontend.🧑‍💻
- Jorge Eduerdo Tello: Castillo: Desarrollo e implementación de backend, base de datos y servidor.💀
- Emilianos Leos Beas: Desarrollo de la interfaz.🧑‍💻
- **ChatGPT (MVP)**: Apoyo técnico, debugging y soporte en tiempo récord. 🚀

---

¡Gracias por revisar nuestro proyecto! 🎉
