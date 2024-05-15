function mostrarModal(id) {
    $('#modalConfirmarRechazar').modal('show');
    $('#modalConfirmarRechazar').find('.btn-primary').attr('data-id', id);
    $('#modalConfirmarRechazar').find('.btn-secondary').attr('data-id', id);
}

function confirmarCita(button) {
    var id = button.getAttribute('data-id');
    var dni = document.getElementById('dni_' + id).innerText.split(': ')[1];
    var fecha = document.getElementById('fecha_' + id).value;
    var hora = document.getElementById('hora_' + id).value;
    var duracion = document.getElementById('duracion').value;


    $.ajax({
        url: '/doctor/citas/asignar-cita', 
        type: 'POST',
        data: {
            dni: dni,
            fecha: fecha,
            hora: hora,
            duracion: duracion
        },
        success: function(response) {
            console.log('Cita asignada exitosamente:', response);
            alert("Cita confirmada");

            $.ajax({
                url: '/doctor/eliminar-notificacion', 
                type: 'POST', 
                data: {
                    id: id
                },
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
        }, 
        success: function(response) {
            console.log('Cita eliminada exitosamente:', response);
            window.location.reload();
            
        },
        error: function(xhr, status, error) {
            var errorMessage = xhr.responseText;
            console.error('Error al eliminar la cita:', errorMessage);
        }
    });

    $('#modalConfirmarRechazar').modal('hide');
}

