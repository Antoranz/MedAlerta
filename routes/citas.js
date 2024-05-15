var express = require('express');
var router = express.Router();
const DAODoctor = require("../DAODoctores")
const DAOPaciente = require("../DAOPacientes")

const mysql = require('mysql');
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'medalerta'
});

const dao = new DAODoctor(pool);
const dao2 = new DAOPaciente(pool);

router.get("/obtener-citas",async function(req, res,next){
    var citas = await dao.checkCitas(req.session.currentUser.dni);
    var eventos=[];
    console.log(citas);
    citas.forEach(function(c){
      const fechaHoraObj = new Date(c.fecha_hora); //Crear objeto Date directamente desde c.fecha_hora
      const nuevaFechaHora = new Date(fechaHoraObj.getTime() + c.duracion * 60000); //Sumar duración en milisegundos
      eventos.push({
        "id":c.id,
        "title": `Cita médica con ${c.nombre_paciente} ${c.apellidos_paciente}`,
        "start": c.fecha_hora,
        "end": nuevaFechaHora
      });
    });
    res.json(eventos);
});
  
router.post("/eliminar-cita",async function(req, res,next){
const citaId = req.body.id;
    try {

        await dao.eliminarCita(citaId);

        res.status(200).json({ message: 'La cita se eliminó correctamente.' });
    } catch (error) {

        res.status(500).json({ error: 'Se produjo un error al intentar eliminar la cita.' });
    }
});

function formatearFecha(fechaString, horaString) {
  //Separamos la fecha en componentes
  var fechaComponentes = fechaString.split('/');
  var year = parseInt(fechaComponentes[2]);
  var month = parseInt(fechaComponentes[1]);
  var day = parseInt(fechaComponentes[0]);

  //Separamos la hora en componentes
  var horaComponentes = horaString.split(':');
  var hours = parseInt(horaComponentes[0]);
  var minutes = parseInt(horaComponentes[1]);
  var seconds = parseInt(horaComponentes[2]);

  var fecha = new Date(year, month - 1, day, hours, minutes, seconds);

  year = fecha.getFullYear();
  month = agregarCero(fecha.getMonth() + 1);
  day = agregarCero(fecha.getDate());
  hours = agregarCero(fecha.getHours());
  minutes = agregarCero(fecha.getMinutes());
  seconds = agregarCero(fecha.getSeconds());

  var fechaFormateada = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;

  return fechaFormateada;
}

function agregarCero(numero) {
  //Agregar un cero delante si el número es menor que 10
  return numero < 10 ? '0' + numero : numero;
}

router.post("/asignar-cita", async function(req, res, next) {
    const dni = req.body.dni;
    const nombrePaciente = req.body.nombrePaciente
    const apellidosPaciente = req.body.apellidosPaciente
    const fecha = req.body.fecha;
    const hora = req.body.hora;
    const duracion = req.body.duracion;
    const fechaHoraString = fecha + ' ' + hora;
    var nuevoDate = new Date(fechaHoraString);
    const fechaActual = new Date();
    var fecha_hora = "";

    var errorMessage;
    
    if (nuevoDate == "Invalid Date") {
  
      var fechaFormateada = formatearFecha(fecha,hora);

      fecha_hora = fechaFormateada;

  
    }else{
      
      //Construir la cadena de texto en el formato 'YYYY-MM-DD HH:MM:SS'
      const year = nuevoDate.getFullYear();
      const month = String(nuevoDate.getMonth() + 1).padStart(2, '0'); 
      const day = String(nuevoDate.getDate()).padStart(2, '0');
      const hour = String(nuevoDate.getHours()).padStart(2, '0');
      const minutes = String(nuevoDate.getMinutes()).padStart(2, '0');
      const seconds = String(nuevoDate.getSeconds()).padStart(2, '0');


      const fechaParaBD = `${year}-${month}-${day} ${hour}:${minutes}:${seconds}`;

      fecha_hora = fechaParaBD;
    
  
    }
  
    try {
      if (!/^\d{8}[a-zA-Z]$/.test(dni)) {
        errorMessage = "Formato de DNI inválido"
        throw new Error("Formato de DNI inválido");
      }
  
      const paciente = await dao2.obtenerPaciente(dni);
      if (paciente.length === 0) {
        errorMessage = "El paciente no existe"
        throw new Error("El paciente no existe");
      }
  
      if (duracion <= 0 || duracion > 60) {
        errorMessage = "La duración de la cita debe estar entre 1 y 60 minutos"
        throw new Error("La duración de la cita debe estar entre 1 y 60 minutos");
      }
  
      if (nuevoDate <= fechaActual) {
        errorMessage = "La fecha y hora seleccionadas deben ser posteriores a la fecha y hora actual"
        throw new Error("La fecha y hora seleccionadas deben ser posteriores a la fecha y hora actual");
      }
     
      var citasCoincidentes = await dao.checkearCitasCoincidentes(req.session.currentUser.dni, fecha_hora, duracion);
      if (citasCoincidentes.length > 0) {
        errorMessage = "Ya existe una cita en ese intervalo de tiempo"
        throw new Error("Ya existe una cita en ese intervalo de tiempo");
      }
      
      await dao.asignarCita(req.session.currentUser.dni, dni, fecha_hora, duracion);
      
      res.render('calendario', { nombre: req.session.currentUser.nombre });
    } catch (error) {
      errorMessage= "Error al asignar cita: "+ errorMessage
      res.render("funcionesUsuario",{ nombre:req.session.currentUser.nombre,nombrePaciente:nombrePaciente,apellidosPaciente:apellidosPaciente,dni:dni, correct:"", error:errorMessage});
    }
  });

module.exports = router;