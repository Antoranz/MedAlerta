var express = require('express');
var router = express.Router();
const DAODoctor = require("../DAODoctores")
const DAOPaciente = require("../DAOPacientes")

const mysql = require('mysql');

// Configuración de la conexión a la base de datos MySQL en XAMPP
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'medalerta'
});


const dao = new DAODoctor(pool);
const dao2 = new DAOPaciente(pool);


/* GET home page. */
router.get('/', function(req, res, next) {
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('index', { nombre:"" });
  }else{
    res.render('main', { nombre: req.session.currentUser.nombre });
  }
});

router.get('/calendario', function(req, res, next) {
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('index', { nombre:"" });
  }else{
    res.render('calendario', { nombre: req.session.currentUser.nombre });
  }
});

router.post("/eliminar-notificacion", async function(req, res, next) {
  const id_card = req.body.id;
 
  try{

    await dao.eliminarNotificacion(id_card); 

    var notificaciones = await dao.obtenerNotificaciones(req.session.currentUser.dni);

    res.render('gestionNotificaciones', { nombre: req.session.currentUser.nombre, notificaciones:notificaciones });

  }catch (error) {
    console.error("Error al asignar cita:", error.message);
    res.status(400).send(error.message);
  }
});

router.get('/gestion-usuarios', function(req, res, next) {
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('index', { nombre:"" });
  }else{
    res.render('gestionUsuarios', { nombre: req.session.currentUser.nombre });
  }
});



router.get('/gestion-notificaciones', async function(req, res, next) {
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('index', { nombre:"" });
  }else{

    var notificaciones = await dao.obtenerNotificaciones(req.session.currentUser.dni);

    notificaciones.forEach(notificacion => {
      const fechaHora = new Date(notificacion.fecha_hora);
      // Asignar la fecha y la hora por separado a la notificación
      notificacion.fecha = fechaHora.toLocaleDateString();
      notificacion.hora = fechaHora.toLocaleTimeString();

      notificacion.id = notificacion.id;

  });

  console.log(notificaciones)

    res.render('gestionNotificaciones', { nombre: req.session.currentUser.nombre, notificaciones:notificaciones });
  }
});

router.get('/funciones-paciente/:dni', (req, res) => {
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('index', { nombre:"" });
  }else{
    const dniUsuario = req.params.dni;
    res.render("funcionesUsuario",{ nombre:req.session.currentUser.nombre,dni:dniUsuario});
  }
});
router.get('/obtener-doctores', (req, res) => {
  const sql = 'SELECT * FROM doctores';

  pool.query(sql, (err, result) => {
      if (err) {
          console.error('Error al obtener doctores: ' + err.message);
          res.status(500).send('Error interno del servidor');
      } else {
          res.json(result);
      }
  });
});






router.post("/aniadirUsuario", async function(req, res, next) {
  
  const usuarioP = req.body.dni;

  if(usuarioP.includes("@")){ //
    
    var paciente = await dao2.obtenerPaciente_email(usuarioP);
    
    if(paciente.length!==0){
      console.log("aniado paciente con dni:" + paciente[0].dni + " al doctor con dni: "+ req.session.currentUser.dni);
      dao.aniadirUsuario(req.session.currentUser.dni, paciente[0].dni);

    }

  }else{

    var paciente = await dao2.obtenerPaciente(usuarioP);
    if(paciente.length !==0){
      console.log("aniado paciente con dni:" + paciente[0].dni + " al doctor con dni: "+ req.session.currentUser.dni);
      dao.aniadirUsuario(req.session.currentUser.dni,usuarioP)
    }
   
  }

  res.render('gestionUsuarios',{nombre : req.session.currentUser.nombre});

});

router.post("/guardarTratamiento", async function(req, res, next) {

  
  const dniPaciente = req.body.dniPaciente;
  const descripcion = req.body.descripcion;
  

  const result = await dao.guardarTratamiento(req.session.currentUser.dni,dniPaciente,descripcion)

  
  console.log(dniPaciente);
  console.log(result.insertId)


   // varios meidcamentos en forma de array
   if (Array.isArray(req.body['medicamento[]'])) {
    for (let i = 0; i < req.body['medicamento[]'].length; i++) {
        const medicamento = req.body['medicamento[]'][i];
        const dosis = req.body['dosis[]'][i];
        const horaPrimeraToma = req.body['hora_primera_toma[]'][i];
        const tomasAlDia = req.body['tomas_al_dia[]'][i];
        const fechaInicio = req.body['fecha_inicio[]'][i];
        const fechaFin = req.body['fecha_fin[]'][i];

        console.log(medicamento);
        console.log(dosis);
        console.log(horaPrimeraToma);
        console.log(tomasAlDia);
        console.log(fechaInicio);
        console.log(fechaFin);

        await dao.guardarAlarma(result.insertId, medicamento, dosis, horaPrimeraToma, tomasAlDia, fechaInicio, fechaFin);
    }
} else { //Sólo un medicamento
    const medicamento = req.body['medicamento[]'];
    const dosis = req.body['dosis[]'];
    const horaPrimeraToma = req.body['hora_primera_toma[]'];
    const tomasAlDia = req.body['tomas_al_dia[]'];
    const fechaInicio = req.body['fecha_inicio[]'];
    const fechaFin = req.body['fecha_fin[]'];

    console.log(medicamento);
    console.log(dosis);
    console.log(horaPrimeraToma);
    console.log(tomasAlDia);
    console.log(fechaInicio);
    console.log(fechaFin);

    await dao.guardarAlarma(result.insertId, medicamento, dosis, horaPrimeraToma, tomasAlDia, fechaInicio, fechaFin);
}

  res.render('gestionUsuarios',{nombre : req.session.currentUser.nombre});

});

router.get('/getUsuarios/', async function(req, res, next) {
  try {

    var pacientes = await dao.obtenerPacientes_doctor(req.session.currentUser.dni);

    pacientes = pacientes.map(paciente => {
      return {
          Nombre: paciente.Nombre,
          Apellidos: paciente.Apellidos,
          email: paciente.email,
          dni: paciente.dni
      };
  });
    res.json(pacientes);
  } catch (error) {
    console.error("Error al obtener usuarios:", error);
    res.status(500).json({ error: "Error al obtener usuarios" });
  }
});

router.get('/eliminarAsociacion/:dni', async function(req, res, next) {
  try {

    const dniUsuario = req.params.dni;

    console.log(dniUsuario)


    dao.eliminarAsociacion(req.session.currentUser.dni,dniUsuario)


    res.render('gestionUsuarios',{nombre : req.session.currentUser.nombre});



  } catch (error) {
    
    console.error("Error al obtener usuarios:", error);
  }
});

var usuarioRouter = require('./usuario');
router.use('/usuario', usuarioRouter);

var citasRouter = require('./citas');
router.use('/citas', citasRouter);

var consultaRouter = require('./consulta');
router.use('/consulta', consultaRouter);

var historialRouter = require('./historial');
router.use('/historial', historialRouter);


module.exports = {
  router
};
