// Función para mostrar el modal con el mensaje de confirmación
function mostrarModal(id) {
    $('#modalConfirmarRechazar').modal('show');
    $('#modalConfirmarRechazar').find('.btn-primary').attr('data-id', id);
    $('#modalConfirmarRechazar').find('.btn-secondary').attr('data-id', id);
}

function confirmarCita(button) {
    // Recopilar los valores del formulario utilizando los identificadores únicos
    var id = button.getAttribute('data-id');
    var dni = document.getElementById('dni_' + id).innerText.split(': ')[1];
    var fecha = document.getElementById('fecha_' + id).value;
    var hora = document.getElementById('hora_' + id).value;
    var duracion = document.getElementById('duracion').value;

    // Enviar los datos al servidor utilizando AJAX
    $.ajax({
        url: '/doctor/citas/asignar-cita', // Ruta en el servidor para asignar la cita
        type: 'POST', // Método HTTP utilizado
        data: {
            dni: dni,
            fecha: fecha,
            hora: hora,
            duracion: duracion
        }, // Datos a enviar al servidor
        success: function(response) {
            console.log('Cita asignada exitosamente:', response);
            alert("Cita confirmada");

            $.ajax({
                url: '/doctor/eliminar-notificacion', 
                type: 'POST', 
                data: {
                    id: id
                }, // Datos a enviar al servidor
                success: function(response) {
                    console.log('Cita eliminada exitosamente:', response);
                    window.location.reload();
                    
                },
                error: function(xhr, status, error) {
                    console.error('Error al eliminar la cita:', error);
                }
            });

        },
        error: function(xhr, status, error) {
            var errorMessage = xhr.responseText;
            alert('Error al eliminar la cita: '+ errorMessage);
        }
    });

    // Ocultar el modal después de asignar la cita
    $('#modalConfirmarRechazar').modal('hide');
}

function rechazarCita(button) {
    var id = button.getAttribute('data-id');
    console.log("ID de la notificación:", id);

    alert("Cita cancelada");
    
    $.ajax({
        url: '/doctor/eliminar-notificacion', 
        type: 'POST', 
        data: {
            id: id
        }, // Datos a enviar al servidor
        success: function(response) {
            console.log('Cita eliminada exitosamente:', response);
            window.location.reload();
            
        },
        error: function(xhr, status, error) {
            var errorMessage = xhr.responseText;
            console.error('Error al eliminar la cita:', errorMessage);
        }
    });

    // Ocultar el modal después de asignar la cita
    $('#modalConfirmarRechazar').modal('hide');
}

