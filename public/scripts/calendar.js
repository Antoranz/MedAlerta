document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth', // Vista inicial: mensual
        slotDuration: '00:15:00', // Duración de las casillas del calendario en minutos (en este caso, 15 minutos)
        slotLabelInterval: '00:15:00', // Intervalo para mostrar etiquetas de tiempo (en este caso, 15 minutos)
        businessHours: {
            // Horario laboral
            daysOfWeek: [1, 2, 3, 4, 5],
            startTime: '08:00', 
            endTime: '17:00' 
        },
        selectable: true, 
        selectConstraint: 'businessHours', 
        selectOverlap: false, 
        selectAllow: function(selectInfo) {
            var duration = moment.duration(selectInfo.end.diff(selectInfo.start));
            return duration.asMinutes() === 15;
        },
        headerToolbar: {
            left: 'prev,next today', 
            center: 'title', 
            right: 'dayGridMonth,timeGridWeek,timeGridDay' // Botones para alternar vistas
        },
        dateClick: function(info) {
            // Manejador de evento clic en una fecha
            calendar.gotoDate(info.dateStr); // Cambiar a la vista del día clickeado
            calendar.changeView('timeGridDay'); // Cambiar la vista al día
        }
    });
    calendar.on('eventClick', function(info) {
        var evento = info.event;
        // Crear contenido del modal con información del evento
        var modalContent = `
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">${evento.title}</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p>Inicio: ${evento.start}</p>
                    <p>Fin: ${evento.end}</p>
                    <!-- Otros detalles del evento aquí -->
                </div>
                <div class="modal-footer">
                    <button id="eliminarCitaBtn" type="button" class="btn btn-danger">Eliminar Cita</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                </div>
            </div>
        `;
        var modalElement = document.getElementById('myModal'); // Obtener el elemento del modal por su id
        var modalBody = modalElement.querySelector('.modal-body'); // Obtener el cuerpo del modal dentro del elemento modal
        modalBody.innerHTML = modalContent; // Establecer el contenido en el cuerpo del modal
        var modal = new bootstrap.Modal(modalElement); // Crear la instancia del modal con el elemento obtenido
        modal.show(); // Mostrar el modal
        
        
        // Agregar manejador de clic para el botón de eliminar cita
        document.getElementById('eliminarCitaBtn').addEventListener('click', function() {
            eliminarCita(evento.id); // Llamar a la función para eliminar la cita
            modal.hide();
        });
    });
    cargarCitas();
    calendar.render();
    function eliminarCita(eventoId) {
        $.ajax({
            url: '/doctor/citas/eliminar-cita', // Ruta en el servidor para eliminar la cita
            type: 'POST', // Método HTTP utilizado
            data: { id: eventoId }, // Datos a enviar al servidor (el identificador del evento)
            success: function(response) {
                calendar.removeAllEvents();
                cargarCitas();
                console.log('Cita eliminada con éxito');
                // Aquí puedes agregar cualquier otra acción que desees después de eliminar la cita
            },
            error: function(xhr, status, error) {
                console.error('Error al eliminar la cita:', error);
            }
        });
    }
    function cargarCitas(){
        $.ajax({
            url: '/doctor/citas/obtener-citas',
            type: 'GET',
            dataType: 'json',
            success: function(response) {
                console.log(response);
                if (response.length >0) {
                    var eventos = [];
                    response.forEach(function(cita) {
                        eventos.push({
                            id: cita.id,
                            title: cita.title,
                            start: cita.start, 
                            end: cita.end 
                        });
                    });
                    calendar.addEventSource(eventos);
                } else {
                    console.error('No se encontraron citas.');
                }
            },
            error: function(xhr, status, error) {
                console.error('Error al obtener citas:', error);
            }
        });
    }
});

