$(document).ready(function() {
    obtenerCitas();
});

function modalAniadirUsuario() {
    $('#modalAniadirUsuario').modal('show');
}

function obtenerCitas() {
    $.ajax({
        url: "/doctor/citas/obtener-citas",
        type: "GET",
        success: function(data) {
            var citas = $("#citas")
            var html = "";
            data.forEach(function(evento) {
                const startFormatted = formatearFecha(evento.start);
                const endFormatted = formatearFecha(evento.end); 

                html += `
                    <div class="card mb-3">
                        <div class="card-body">
                            <h5 class="card-title">${evento.title}</h5>
                            <p class="card-text">
                                <strong>Fecha:</strong> ${startFormatted.dia}<br>
                                <strong>Inicio:</strong> ${startFormatted.hora}<br>
                                <strong>Fin:</strong> ${endFormatted.hora}
                            </p>
                        </div>
                    </div>
                </div>
                `;
            });
            $("#citas").html(html);
        },
        error: function(xhr, status, error) {
            console.error("Error obteniendo citas:", error);
            $("#citas").text("Error obteniendo citas.");
        }
    });
}


function formatearFecha(fechaAntigua) {
    const fecha = new Date(fechaAntigua);
    const dia = fecha.toLocaleDateString();
    const hora = fecha.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

    return { dia, hora };
}