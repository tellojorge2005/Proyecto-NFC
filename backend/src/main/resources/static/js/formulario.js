async function enviarFormulario(event) {
    event.preventDefault(); // Evita el envío automático del formulario

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
    formData.append("nfc", document.getElementById("nfc").value);
    formData.append("foto", document.getElementById("subir-foto").files[0]);

    const response = await fetch('/api/registrar-alumno', {
        method: 'POST',
        body: formData
    });

    const data = await response.json();
    alert(data.mensaje); // Muestra el mensaje de respuesta
}