

function cargarEnfermedades(dni) {
    $.ajax({
        url: '/doctor/usuario/getEnfermedades', 
        method: 'GET',
        success: function(data) {
            const contenedor = $('#enfermedadesContainer');
            contenedor.empty();

            const numeroColumnas = 3;
            const columnas = [];

            for (let i = 0; i < numeroColumnas; i++) {
                const columna = $('<div></div>').addClass('col-md-4');
                columnas.push(columna);
                contenedor.append(columna);
            }

            data.forEach((item, index) => {
                const div = $('<div></div>').addClass('form-check');

                const checkbox = $('<input>')
                    .attr('type', 'checkbox')
                    .attr('class', 'form-check-input')
                    .attr('id', `item-${index}`)
                    .attr('name', `${item}`);

                const label = $('<label></label>')
                    .attr('for', `item-${index}`) 
                    .addClass('form-check-label')
                    .text(item);

                div.append(checkbox).append(label);

                const columnaI = index % numeroColumnas; 
                columnas[columnaI].append(div);
            });
        },
        error: function(xhr, status, error) {
            console.error("Error al obtener datos:", error);
        }
    });
}

document.addEventListener('DOMContentLoaded', function() {

    var dniDoctor = "<%=dni %>"; 
    cargarEnfermedades(dniDoctor);
    
    document.getElementById('agregarEnfermedad').addEventListener('click', function() {
        var nuevaEnfermedad = document.getElementById('nuevaEnfermedad').value.trim();

        if (nuevaEnfermedad !== '') {

            var checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'form-check-input';
            checkbox.id = nuevaEnfermedad.toLowerCase().replace(/\s+/g, '');
            checkbox.name = nuevaEnfermedad.toLowerCase().replace(/\s+/g, '');

            var label = document.createElement('label');

            label.className = 'form-check-label';
            label.setAttribute('for', nuevaEnfermedad.toLowerCase().replace(/\s+/g, ''));
            label.textContent = nuevaEnfermedad;

            var divCheckbox = document.createElement('div');

            divCheckbox.className = 'form-check';
            divCheckbox.appendChild(checkbox);
            divCheckbox.appendChild(label);

            var columnas = document.querySelectorAll('#enfermedadesContainer > div');
            var minColumn = columnas[0];
            var minCount = minColumn.children.length;

            columnas.forEach((col) => {
                var count = col.children.length;
                if (count < minCount) {
                    minColumn = col;
                    minCount = count;
                }
            });

            minColumn.appendChild(divCheckbox);
            document.getElementById('nuevaEnfermedad').value = '';
            console.log("Enfermedad añadida: " + nuevaEnfermedad);

            $.ajax({
                url: '/doctor/usuario/aniadirEnfermedad', 
                method: 'POST',
                data: { enfermedad: nuevaEnfermedad },
                success: function(data) {
                },
                error: function(xhr, status, error) {
                    console.error("Error al añadir enfermedad:", error);
                }
            });
        }
    });

});

