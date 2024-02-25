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
        eventClick: function(info) {
            // Manejador de evento clic en un evento
            alert('Evento: ' + info.event.title);
        },
        dateClick: function(info) {
            // Manejador de evento clic en una fecha
            calendar.gotoDate(info.dateStr); // Cambiar a la vista del día clickeado
            calendar.changeView('timeGridDay'); // Cambiar la vista al día
        }
    });
     $.ajax({
        url: '/doctor/obtener-citas',
        type: 'GET',
        dataType: 'json',
        success: function(response) {
            console.log(response);
            if (response.length >0) {
                var eventos = [];
                response.forEach(function(cita) {
                    eventos.push({
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
    calendar.render();
});
