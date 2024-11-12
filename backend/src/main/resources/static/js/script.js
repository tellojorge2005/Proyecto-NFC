let temporizador;

function iniciarLecturaPeriodica() {
    setInterval(leerNFC, 2000); // Llama a la función leerNFC cada 2 segundos.
}

function leerNFC() {
    fetch('/api/leer-nfc')
        .then(response => response.json())
        .then(data => {
            if (data.nombre) {
                // Si la tarjeta es reconocida, muestra la información del alumno
                document.getElementById('nombre').innerHTML = `<strong>${data.nombre} ${data.apellidos}</strong>`;
                document.getElementById('matricula').innerText = data.matricula;
                document.getElementById('foto').src = data.fotografia;
                document.getElementById('nivel').innerText = data.grado_academico;
                document.getElementById('campus').innerText = data.campus;
                document.getElementById('resultado').innerText = "Bienvenido :)";

                // Reinicia el temporizador para restablecer la información
                reiniciarTemporizador();
            } else if (data.mensaje === "Acceso denegado") {
                // Si el mensaje es "Acceso denegado", muestra el mensaje en el resultado
                document.getElementById('nombre').innerHTML = `<strong>Acceso denegado</strong>`;
                document.getElementById('matricula').innerText = "";
                document.getElementById('foto').src = "/img/DENEGADO.png";
                document.getElementById('nivel').innerText = "";
                document.getElementById('campus').innerText ="";
                document.getElementById('resultado').innerText ="";
                reiniciarTemporizador();

            } else {
                // Si hay otro mensaje o error
                document.getElementById('resultado').innerText = data.mensaje || "Error desconocido";
            }
        })
        .catch(error => console.error('Error al leer NFC:', error));
}

function reiniciarTemporizador() {
    if (temporizador) clearTimeout(temporizador); // Limpia el temporizador anterior
    temporizador = setTimeout(restablecerInformacionDefault, 3000); // Restablece después de 3 segundos
}

function restablecerInformacionDefault() {
    document.getElementById('nombre').innerHTML = `<strong>Nombre del usuario</strong>`;
    document.getElementById('matricula').innerText = "Matrícula";
    document.getElementById('nivel').innerText = "Nivel";
    document.getElementById('campus').innerText = "Campus";
    document.getElementById('foto').src = "img/USUARIO.png"; // Imagen predeterminada
    document.getElementById('resultado').innerText = "Esperando lectura...";
}

window.onload = iniciarLecturaPeriodica;
