// Función para detectar automáticamente el UID del NFC y actualizar el campo de input
async function detectarUID() {
    setInterval(async () => {
        try {
            const response = await fetch('/api/leer-uidformulario'); // Llama al endpoint para leer el UID
            const data = await response.json();

            if (data.uid) {
                // Actualiza el campo NFC con el UID detectado
                const nfcInput = document.getElementById("nfc");
                nfcInput.value = data.uid;
            } else {
                console.warn(data.mensaje); // Muestra el mensaje de advertencia si no hay UID
            }
        } catch (error) {
            console.error("Error al leer el UID del NFC:", error);
        }
    }, 2000); // Intervalo de 2 segundos
}

// Función para enviar el formulario
async function enviarFormulario(event) {
    event.preventDefault(); // Evita el envío automático del formulario

    const nfc = document.getElementById("nfc").value;
    if (!nfc) {
        alert("No se detectó ningún UID de NFC. Por favor, pase la tarjeta.");
        return; // Detiene el envío si no hay UID
    }

    const formData = new FormData();
    formData.append("nombre", document.getElementById("nombre").value);
    formData.append("apellidos", document.getElementById("apellidos").value);
    formData.append("grado", document.getElementById("grado").value);
    formData.append("semestre", document.getElementById("semestre").value);
    formData.append("matricula", document.getElementById("matricula").value);
    formData.append("telefono", document.getElementById("telefono").value);
    formData.append("correo", document.getElementById("correo").value);
    formData.append("contraseña", document.getElementById("contraseña").value);
    formData.append("materia", document.getElementById("materia").value);
    formData.append("campus", document.getElementById("campus").value);
    formData.append("nfc", nfc); // Agrega el UID detectado
    formData.append("foto", document.getElementById("subir-foto").files[0]);

    try {
        const response = await fetch('/api/registrar-alumno', {
            method: 'POST',
            body: formData
        });

        const data = await response.json();
        alert(data.mensaje); // Muestra el mensaje de respuesta
    } catch (error) {
        console.error("Error al enviar el formulario:", error);
        alert("Hubo un problema al enviar el formulario. Por favor, inténtelo de nuevo.");
    }
}

// Llama a la función de detección de UID al cargar la página
document.addEventListener("DOMContentLoaded", () => {
    detectarUID(); // Inicia la detección automática del UID
});
