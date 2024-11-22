// Cargar la lista de estudiantes al cargar la página
document.addEventListener("DOMContentLoaded", async () => {
    const tabla = document.getElementById("tabla-estudiantes");

    // Obtener la materia seleccionada desde sessionStorage
    const materia = sessionStorage.getItem("materiaSeleccionada");

    if (!materia) {
        alert("No se seleccionó ninguna materia");
        window.location.href = "index.html"; // Redirige si no hay materia seleccionada
        return;
    }

    try {
        // Carga la lista de estudiantes
        const response = await fetch(`/api/lista-estudiantes?materia=${encodeURIComponent(materia)}`);
        const estudiantes = await response.json();

        // Genera las filas de la tabla dinámicamente
        estudiantes.forEach((estudiante, index) => {
            const fila = document.createElement("tr");

            // Columna de nombre
            const columnaNombre = document.createElement("td");
            columnaNombre.textContent = `${estudiante.nombre} ${estudiante.apellidos}`;
            fila.appendChild(columnaNombre);

            // Columna de checkbox
            const columnaCheckbox = document.createElement("td");
            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.name = `asistencia${index + 1}`;
            checkbox.disabled = false; // Deshabilitar para evitar cambios manuales
            if (estudiante.asistencia === 1) {
                checkbox.checked = true; // Marca si asistencia es 1
            }
            columnaCheckbox.appendChild(checkbox);
            fila.appendChild(columnaCheckbox);

            // Agrega la fila a la tabla
            tabla.appendChild(fila);
        });

        // Inicia la lectura periódica del NFC
        iniciarLecturaNFC(materia);
    } catch (error) {
        console.error("Error al cargar la lista de estudiantes:", error);
    }
});

// Leer NFC y marcar asistencia periódicamente
/*async function iniciarLecturaNFC(materia) {
    setInterval(async () => {
        try {
            const response = await fetch('/api/leer-nfc');
            const data = await response.json();

            if (data.nfc) {
                // Enviar solicitud para marcar asistencia
                const marcarResponse = await fetch('/api/marcar-asistencia', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `nfc=${encodeURIComponent(data.nfc)}&materia=${encodeURIComponent(materia)}`,
                });

                const marcarResult = await marcarResponse.json();
                alert(marcarResult.mensaje);

                // Actualiza la interfaz si la asistencia se marcó con éxito
                if (marcarResult.mensaje === "Asistencia marcada con éxito") {
                    const filas = document.querySelectorAll("#tabla-estudiantes tr");
                    filas.forEach((fila) => {
                        const nombreColumna = fila.cells[0]?.textContent;
                        if (nombreColumna === `${data.nombre} ${data.apellidos}`) {
                            const checkbox = fila.cells[1]?.querySelector("input[type='checkbox']");
                            if (checkbox) checkbox.checked = true; // Marca el checkbox
                        }
                    });
                }
            }
        } catch (error) {
            console.error("Error al procesar el NFC:", error);
        }
    }, 2000); // Intervalo de 2 segundos
}*/
