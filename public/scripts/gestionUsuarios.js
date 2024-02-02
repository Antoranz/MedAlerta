
$(document).ready(function () {
    var usuarios = [];
    var usuariosFiltrados = [];
    var filtro = "Nombre";
    var buscador = $("#buscador");
    var drop = $("#drop");
    var correo = $("#email");
    
    function cargarBuscador(){//carga el buscador para filtrar
        buscador.empty();
        drop.empty();
        var $search = $('<input class="form-control me-2" id="textSearch" type="text" placeholder="Buscar" value="" name="searchInput">');
        $search.on('keypress', function (e) {
            if (e.which === 13) { // 13 -> tecla Enter
              nuevaListaFiltrada();
            }
        });
        buscador.append($search);

        var $buttonNombre = $('<button class="dropdown-item" type="button">Nombre</button>');
        $buttonNombre.on('click', function(){
            filtro = "Nombre";
            $("#filtro").text("Nombre");
        });

        var $buttonApellidos = $('<button class="dropdown-item" type="button">Apellidos</button>');
        $buttonApellidos.on('click', function(){
            filtro = "Apellidos";
            $("#filtro").text("Apellidos");
        });

        var $buttonCorreo = $('<button class="dropdown-item" type="button">Correo</button>');
        $buttonCorreo.on('click', function(){
            filtro = "Correo";
            $("#filtro").text("Correo");
        });

        var $buttonDNI = $('<button class="dropdown-item" type="button">DNI</button>');
        $buttonDNI.on('click', function(){
            filtro = "DNI";
            $("#filtro").text("DNI");
        });


        // Creación del menú desplegable
        var $dropdown = $('<div class="dropdown">' +
            '<button class="btn btn-secondary dropdown-toggle" id="filtro" type="button" data-bs-toggle="dropdown" aria-expanded="false">' +
            'Filtrado' +
            '</button>' +
            '<ul class="dropdown-menu">' +
            '<li></li>' + 
            '<li></li>' + 
            '<li></li>' + 
            '<li></li>'+
            '</ul>' +
            '</div>');

        // Agregar los botones al espacio correspondiente en el menú desplegable
        $dropdown.find('li').eq(0).append($buttonNombre);
        $dropdown.find('li').eq(1).append($buttonApellidos);
        $dropdown.find('li').eq(2).append($buttonCorreo);
        $dropdown.find('li').eq(3).append($buttonDNI);

        drop.append($dropdown);

    }
    function mostrarUsuarios() {
        console.log("xxxx");
        console.log(usuariosFiltrados)
        var $tabla = $('#tablaHistorial');
        $tabla.empty();
        usuariosFiltrados.forEach(function(usuario, index) {          
            var $buttonEliminar = $('<button class="btn btn-primary" type="button">Eliminar</button>');
            $buttonEliminar.on('click', function(){
                $.ajax({
                    url: `/doctor/eliminarAsociacion/${usuario.DNI}/${correo}`,
                    type: 'POST',
                    success:function(data){
                        getUsuarios();
                        cargarBuscador();
                        mostrarUsuarios();
                        console.info("ok");
                    }, error: function(error){
                        mostrarUsuarios();
                        console.info("error");
                    }
                });
            });
            var $buttonCrearHistorial = $('<button class="btn btn-primary" type="button">Crear historial médico</button>');
            $buttonCrearHistorial.on('click', function(){
                window.location.href = '/doctor/verHistorial/' + usuario.DNI + '/' + usuario.id;
            });

            
            var $buttonMostrar = $('<button class="btn btn-primary" type="button">Mostrar historial médico</button>');
            $buttonMostrar.on('click', async function(){
                $.ajax({
                    url: '/doctor/obtenerURLPDF', // Cambia esto con la ruta correcta en tu servidor
                    type: 'GET',
                    success: function(response) {
                      // Cuando la solicitud AJAX sea exitosa, muestra el PDF en el contenedor
                      mostrarPDF(response.downloadURL);
                    },
                    error: function() {
                      console.error('Error al obtener la URL del PDF');
                      alert('Error al cargar el PDF');
                    }
                  });
            });

            function mostrarPDF(downloadURL) {
                // Crea un elemento <iframe> para mostrar el PDF
                var $iframe = $('<iframe></iframe>');
                $iframe.attr('src', downloadURL);
                $iframe.css({
                  width: '100%',
                  height: '600px'  // Ajusta la altura según tus necesidades
                });
              
                // Limpia cualquier contenido anterior en el contenedor y agrega el <iframe>
                var $contenedorPDF = $('#contenedorPDF');
                $contenedorPDF.empty();
                $contenedorPDF.append($iframe);
              }
            

            const $fila = $(`<tr>
            <th scope="row">${index + 1}</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>`);
        
        $fila.find('td').eq(0).append(usuario.Nombre);
        $fila.find('td').eq(1).append(usuario.Apellidos);
        $fila.find('td').eq(2).append(usuario.email);
        $fila.find('td').eq(3).append(usuario.dni);
        $fila.find('td').eq(4).append($buttonEliminar);
        $fila.find('td').eq(5).append($buttonCrearHistorial);
        $fila.find('td').eq(6).append($buttonMostrar);
        
        $('#tablaHistorial').append($fila);
        
        });
    }
    function getUsuarios() {
        $.ajax({
            url: `/doctor/getUsuarios/`,
            type: 'GET',
            success: function(data) {
               usuarios = data;
               usuariosFiltrados = data;
               cargarBuscador();
               $("#filtro").text("Nombre");
               mostrarUsuarios();
               
            },
            error: function(error) {
                console.error('Error en la solicitud AJAX:', error);
            }
        });
    }
    function nuevaListaFiltrada(){
        usuariosFiltrados= [];
        console.log(usuarios);
        switch (filtro) {
            case "Apellidos":
                usuarios.forEach(function (u){
                    var palabrasApellidos = u.Apellidos.split(' ');
                    var encontrado = false;
                    palabrasApellidos.forEach(function (p){
                        if (p.toLowerCase().startsWith($("#textSearch").val().toLowerCase())) {
                            encontrado = true;
                            return;
                        }   
                    });
                    if(encontrado === true){
                        usuariosFiltrados.push(u);
                    }   
                });
                break;
            case "Correo":
                usuarios.forEach(function (u){
                    if (u.email.toLowerCase().startsWith( $("#textSearch").val().toLowerCase())) {
                        usuariosFiltrados.push(u);
                    } 
                });
                break;
            case "DNI":
                usuarios.forEach(function (u){
                    if (u.dni.toLowerCase().startsWith( $("#textSearch").val().toLowerCase())) {
                        usuariosFiltrados.push(u);
                    } 
                });
                break;    
            default: //nombre
                usuarios.forEach(function (u){
                    var palabrasNombre = u.Nombre.split(' ');
                    var encontrado = false;
                    palabrasNombre.forEach(function (p){
                        if (p.toLowerCase().startsWith($("#textSearch").val().toLowerCase())) {
                            encontrado = true;
                            return;
                        }
                    });
                    if(encontrado == true) usuariosFiltrados.push(u);;
                });
                break;
        }
        mostrarUsuarios();
    }
    var $contenedor = $('#Contenedor');
    var $button = $('<button type="button" class="btn btn-primary">' + "Refrescar Usuarios" + '</button>');
    $button.on('click', function(){
        getUsuarios();
        cargarBuscador();
        mostrarUsuarios();
    });
    $contenedor.append($button);
    getUsuarios();
    
});