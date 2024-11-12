let temporizador;
const mensajeElemento = document.getElementById('mensaje');
async function login() {
    const correo = document.getElementById('correo').value;
    const contraseña = document.getElementById('contraseña').value;

    const response = await fetch('/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ correo, contraseña }),
    });

    const data = await response.json();


    if (data.mensaje === "Autenticación exitosa") {
        mensajeElemento.textContent = "Espere...";
        // Redirigir según el rol
        if (data.rol === "Administrador") {
            mensajeElemento.textContent = "Autenticación exitosa";
            mensajeElemento.style.color = "green";
            window.location.href = "/admin.html"; // Página para administradores
        } else if (data.rol === "Profesor") {
            mensajeElemento.textContent = "Autenticación exitosa";
            mensajeElemento.style.color = "green";
            window.location.href = "/tablero.html"; // Página para profesores
        } else if (data.rol === "Profesional"||data.rol === "Preparatoria") {
            mensajeElemento.textContent = "Acceso denegado";
            mensajeElemento.style.color = "orange";
            mensajeElemento.style.fontSize = "1rem";
            mensajeElemento.style.fontWeight = "bold";
            reiniciarTemporizador();
        }
    } else {
        mensajeElemento.textContent = data.mensaje;
        mensajeElemento.style.color = "red";
        mensajeElemento.style.fontSize = "1rem";
        mensajeElemento.style.fontWeight = "bold";
        reiniciarTemporizador();
    }
}
function reiniciarTemporizador() {
    if (temporizador) clearTimeout(temporizador); // Limpia el temporizador anterior
    temporizador = setTimeout(restablecerInformacionDefault, 5000); // Restablece después de 3 segundos
}

function restablecerInformacionDefault() {
    mensajeElemento.textContent = "";
}
