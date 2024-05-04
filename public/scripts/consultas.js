var consultaActualId = "";
var consultaActualTitulo = "";
function getMensajes(consultaId, consultaTitulo) {
    
    consultaActualId = consultaId;
    consultaActualTitulo = consultaTitulo;
    $.ajax({
        url: '/doctor/consulta/mensajes/' + consultaId,
        type: 'GET',
        data: { consultaId: consultaId, consultaTitulo: consultaTitulo },
        success: function(response) {

            fetch('/doctor/consulta/borrarNotificaciones', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({consultaId: consultaId })
                })
                
            var mensajesDiv = $('#mensajes'); 
            var consultasDiv = $('#consultas'); 
            var campoMensajes = $('#campoMensajes')
            consultasDiv.empty();
            mensajesDiv.show();
            mensajesDiv.children().show();
            

            var TituloConsultas = $('#TituloConsultas'); 
            TituloConsultas.empty();

            var TituloDiv = $('#titulo'); 
            TituloDiv.text(consultaTitulo);

            var FechaDiv = $('#fecha'); 
            FechaDiv.text(new Date().toLocaleDateString('es-ES'));

            var InputMensajesDiv = $('#inputMensajes'); 
            InputMensajesDiv.empty();
            
            campoMensajes.empty();
            actualizarChat(consultaId);
            InputMensajesDiv.append('<form class="form-inline" action="/doctor/consulta/mandarMensaje" method="post">');
            InputMensajesDiv.append('<input type="text" id="mensajeDoctor" placeholder="Escribe tu mensaje...">');
            InputMensajesDiv.append('<button id="enviarBoton" class="btn btn-primary" type="submit">Enviar</button>');
            InputMensajesDiv.append('</form>');

            const enviarBoton = document.getElementById('enviarBoton');
            
            enviarBoton.addEventListener('click', () => {

                const mensajeInput = document.getElementById('mensajeDoctor');
                const texto = mensajeInput.value;
                if(texto != "" && texto != null){
                    $.ajax({
                        url: '/doctor/consulta/mandarMensaje',
                        type: 'POST', 
                        contentType: 'application/json', 
                        data: JSON.stringify({texto: texto, consultaId: consultaId}),
                        success: function (data) { 
                            campoMensajes.append('<p class="casillaDoctor"><em class="mensajeDoctor">' + texto + '</em></p>');
                            mensajeInput.value = "";
                        },
                        error: function (xhr, status, error) {
                            console.error('Error:', error);
                        }
                    });
                }
            });        
        },
        error: function(xhr, status, error) {
            console.error(xhr.responseText);
        }
    });



}
function actualizarChat(consultaId){

    $.ajax({
        url: '/doctor/consulta/mensajes/' + consultaId,
        type: 'GET',
        data: { consultaId: consultaId },
        success: function(response) {
            fetch('/doctor/consulta/borrarNotificaciones', {
                method: 'POST',
                body: JSON.stringify({consultaId: consultaId })
            });
    
            var campoMensajes = $('#campoMensajes');
            campoMensajes.empty();
    
            var mensajes = response.mensajes;
            var ultimoDia = null;
    
            mensajes.forEach(function(element) {
                var fechaISO = element.fecha;
                var fecha = new Date(fechaISO);
                var dia = fecha.toISOString().split('T')[0];
    
                if (ultimoDia !== dia) {
                    campoMensajes.append('<div class="separadorDia">' + dia + '</div>');
                    ultimoDia = dia;
                }
    
                var mensaje = '';
    
                if (element.propietario == 1) {
                    mensaje = '<p><em class="mensajePaciente bg-primary">' + element.mensaje + '</em></p>';
                } else {
                    mensaje = '<p class="casillaDoctor"><em class="mensajeDoctor bg-info">' + element.mensaje + '</em></p>';
                }
    
                campoMensajes.append(mensaje);
            });
        },
        error: function(xhr, status, error) {
            console.error(xhr.responseText);
            console.log("Error del servidor");
        }
    });

}
window.addEventListener('load', function() {
    const intervalo = 5000;
    setInterval(function() {
        if(consultaActualId != ""){
            actualizarChat(consultaActualId);
        }
    }, intervalo);
});