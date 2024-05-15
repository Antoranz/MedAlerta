document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth', //Vista inicial: mensual
        slotDuration: '00:15:00', //Duración de las casillas del calendario en minutos (en este caso, 15 minutos)
        slotLabelInterval: '00:15:00', //Intervalo para mostrar etiquetas de tiempo (en este caso, 15 minutos)
        businessHours: {
            //Horario laboral
            daysOfWeek: [1, 2, 3, 4, 5],
            startTime: '12:00', 
            endTime: '20:00' 
        },
        selectConstraint: 'businessHours', 
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
    });
    calendar.on('eventClick', function(info) {
        var evento = info.event;
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
        var modalElement = document.getElementById('myModal'); 
        var modalBody = modalElement.querySelector('.modal-body'); 
        modalBody.innerHTML = modalContent; 
        var modal = new bootstrap.Modal(modalElement); 
        modal.show(); 
        
        document.getElementById('eliminarCitaBtn').addEventListener('click', function() {
            eliminarCita(evento.id); 
            modal.hide();
        });
    });
    cargarCitas();
    calendarEl.addEventListener('click', cambiarAVistaDiaria);
    calendar.render();
    function eliminarCita(eventoId) {
        $.ajax({
            url: '/doctor/citas/eliminar-cita', 
            type: 'POST',
            data: { id: eventoId },
            success: function(response) {
                calendar.removeAllEvents();
                cargarCitas();
                console.log('Cita eliminada con éxito');
            },
            error: function(xhr, status, error) {
                console.error('Error al eliminar la cita:', error);
            }
        });
    }
    function cambiarAVistaDiaria(event) {
        var tdElement = event.target.closest('.fc-day');
    
        if (tdElement && tdElement.dataset.date) {
            var fechaClickeada = tdElement.dataset.date;
            calendar.gotoDate(fechaClickeada); 
            calendar.changeView('timeGridDay');
        }
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