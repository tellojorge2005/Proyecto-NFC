document.addEventListener("DOMContentLoaded", async () => {
    const tabla = document.getElementById("tabla-estudiantes");
    const materia = sessionStorage.getItem("materiaSeleccionada");

    if (!materia) {
        alert("No se seleccionó ninguna materia");
        window.location.href = "index.html";
        return;
    }

    try {
        const response = await fetch(`/api/lista-estudiantes?materia=${encodeURIComponent(materia)}`);
        const estudiantes = await response.json();

        estudiantes.forEach((estudiante, index) => {
            const fila = document.createElement("tr");
            fila.setAttribute("data-nfc", estudiante.nfc);

            const columnaNombre = document.createElement("td");
            columnaNombre.textContent = `${estudiante.nombre} ${estudiante.apellidos}`;
            fila.appendChild(columnaNombre);

            const columnaCheckbox = document.createElement("td");
            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.name = `asistencia${index + 1}`;
            checkbox.disabled = true;
            checkbox.checked = estudiante.asistencia === 1;
            columnaCheckbox.appendChild(checkbox);
            fila.appendChild(columnaCheckbox);

            tabla.appendChild(fila);
        });

        iniciarLecturaNFC(materia);
    } catch (error) {
        console.error("Error al cargar la lista de estudiantes:", error);
    }
});

async function iniciarLecturaNFC(materia) {
    setInterval(async () => {
        try {
            const response = await fetch('/api/leer-uid');
            const data = await response.json();

            if (data.nfc && data.materia === materia) {
                const filas = document.querySelectorAll("#tabla-estudiantes tr");
                filas.forEach((fila) => {
                    const nombreColumna = fila.cells[0]?.textContent;
                    if (nombreColumna === `${data.nombre} ${data.apellidos}`) {
                        const checkbox = fila.cells[1]?.querySelector("input[type='checkbox']");
                        if (checkbox && !checkbox.checked) {
                            checkbox.checked = true; // Marca el checkbox
                        }
                    }
                });

                // Marca asistencia en la base de datos
                const marcarResponse = await fetch('/api/marcar-asistencia', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `nfc=${encodeURIComponent(data.nfc)}&materia=${encodeURIComponent(materia)}`,
                });

                const marcarResult = await marcarResponse.json();
            }
        } catch (error) {
            console.error("Error al procesar el NFC:", error);
        }
    }, 2000); // Intervalo de 2 segundos
}

