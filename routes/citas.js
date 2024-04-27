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
      const fechaHoraObj = new Date(c.fecha_hora); // Crear objeto Date directamente desde c.fecha_hora
      const nuevaFechaHora = new Date(fechaHoraObj.getTime() + c.duracion * 60000); // Sumar duración en milisegundos
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
        // Aquí realizas la lógica para eliminar la cita de la base de datos
        await dao.eliminarCita(citaId);
        // Si la eliminación fue exitosa, envía una respuesta con el código de estado 200 (Éxito)
        res.status(200).json({ message: 'La cita se eliminó correctamente.' });
    } catch (error) {
        // Si ocurrió algún error durante la eliminación, envía una respuesta con el código de estado 500 (Error del servidor)
        res.status(500).json({ error: 'Se produjo un error al intentar eliminar la cita.' });
    }
});

router.post("/asignar-cita", async function(req, res, next) {
    const dni = req.body.dni;
    const fecha = req.body.fecha;
    const hora = req.body.hora;
    const duracion = req.body.duracion;
    const fechaHoraString = fecha + ' ' + hora; // Combinar fecha y hora en una sola cadena
    var nuevoDate = new Date(fechaHoraString);
    const fechaActual = new Date();
  
    if (nuevoDate == "Invalid Date") {
  
      const fechaParts = fecha.split('/'); // Divide la cadena de fecha por "/"
      const horaParts = hora.split(':'); // Divide la cadena de hora por ":"
  
      // Crea un nuevo objeto Date con los componentes de fecha y hora
      const nuevaFechaaaaaaaa = new Date(
          fechaParts[2], // Año
          parseInt(fechaParts[1]) - 1, // Mes (se resta 1 porque los meses en JavaScript van de 0 a 11)
          fechaParts[0], // Día
          horaParts[0], // Hora
          horaParts[1], // Minuto
          horaParts[2] // Segundo
      );
  
      console.log("Ha entrado en invalid");
  
      nuevoDate = nuevaFechaaaaaaaa;
      console.log(nuevaFechaaaaaaaa)
  
    }else{
      
     const fechaParts = fecha.split('/'); // Divide la cadena de fecha por "/"
      const horaParts = hora.split(':'); // Divide la cadena de hora por ":"
  
      // Crea un nuevo objeto Date con los componentes de fecha y hora
      const nuevaFechaaaaaaaa = new Date(
          fechaParts[2], // Año
          parseInt(fechaParts[1]) - 1, // Mes (se resta 1 porque los meses en JavaScript van de 0 a 11)
          fechaParts[0], // Día
          horaParts[0], // Hora
          horaParts[1], // Minuto
          horaParts[2] // Segundo
      );
  
  
      nuevoDate = nuevaFechaaaaaaaa;
      console.log(nuevaFechaaaaaaaa)
  
  
    }
  
  
    console.log(dni)
    console.log(fecha)
    console.log(hora)
    console.log(duracion)
    console.log(nuevoDate)
  
    try {
      if (!/^\d{8}[a-zA-Z]$/.test(dni)) {
        throw new Error("Formato de DNI inválido");
      }
  
      const paciente = await dao2.obtenerPaciente(dni);
      if (paciente.length === 0) {
        throw new Error("El paciente no existe");
      }
  
      if (duracion <= 0 || duracion > 60) {
        throw new Error("La duración de la cita debe estar entre 1 y 60 minutos");
      }
  
      if (nuevoDate <= fechaActual) {
        throw new Error("La fecha y hora seleccionadas deben ser posteriores a la fecha y hora actual");
      }
      console.log("AAAAAAAAAAAAAAAAAAAAA");
      console.log(nuevoDate);
      const fecha_hora = nuevoDate.toISOString();
      console.log("AAAAAAAAAAAAAAAAAAAAA");
      var citasCoincidentes = await dao.checkearCitasCoincidentes(req.session.currentUser.dni, fecha_hora, duracion);
      console.log("citas:", citasCoincidentes);
      if (citasCoincidentes.length > 0) {
        throw new Error("Ya existe una cita en ese intervalo de tiempo");
      }
      
      await dao.asignarCita(req.session.currentUser.dni, dni, nuevoDate, duracion);
      
      res.status(200).send("Cita asignada correctamente");
    } catch (error) {
      console.error("Error al asignar cita:", error.message);
      res.status(400).send(error.message);
    }
  });

module.exports = router;