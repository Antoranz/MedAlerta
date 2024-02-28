// Función para mostrar el modal con el mensaje de confirmación
function mostrarModal(id) {
    $('#modalConfirmarRechazar').modal('show');
    $('#modalConfirmarRechazar').find('.btn-primary').attr('data-id', id);
}

function confirmarCita(button) {
    // Recopilar los valores del formulario utilizando los identificadores únicos
    var id = button.getAttribute('data-id');
    console.log(id)
    var dni = document.getElementById('dni_' + id).innerText.split(': ')[1];
    var fecha = document.getElementById('fecha_' + id).innerText.split(': ')[1];
    var hora = document.getElementById('hora_' + id).innerText.split(': ')[1];
    var duracion = document.getElementById('duracion').value;

    console.log("esta es la fechaaaaaaa:" + fecha)


    console.log(hora)
    // Enviar los datos al servidor utilizando AJAX
    $.ajax({
        url: '/doctor/asignar-cita', // Ruta en el servidor para asignar la cita
        type: 'POST', // Método HTTP utilizado
        data: {
            dni: dni,
            fecha: fecha,
            hora: hora,
            duracion: duracion
        }, // Datos a enviar al servidor
        success: function(response) {
            // Lógica después de asignar la cita (puede ser recargar la lista de citas, etc.)
            console.log('Cita asignada exitosamente:', response);
            // Por ejemplo, si estás utilizando un calendario, puedes recargar los eventos
            alert("Cita confirmada");
            calendar.refetchEvents();
        },
        error: function(xhr, status, error) {
            console.error('Error al asignar la cita:', error);
        }
    });

    // Ocultar el modal después de asignar la cita
    $('#modalConfirmarRechazar').modal('hide');
}
