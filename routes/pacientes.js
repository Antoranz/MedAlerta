var express = require('express');
var router = express.Router();
const nodemailer = require("nodemailer");
const DAOPaciente = require("../DAOPacientes");
const {sendVerificationEmail} = require('../mailer.js');

const crypto = require('crypto');
/* GET home page. */
const mysql = require('mysql');
const { Console } = require('console');

// Configuración de la conexión a la base de datos MySQL en XAMPP
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'medalerta'
});

const dao = new DAOPaciente(pool);





router.post('/crearNotificacionCita/:dni', async function(req, res, next) {

    try {

        var dni_paciente=req.params.dni;

        const {tipo,motivo,fecha,hora,duracion} = req.body;

        var dni_doctor = await dao.obtenerDoctorDelPaciente(dni_paciente);

        console.log(tipo);
        console.log(motivo);
        console.log(fecha);
        console.log(hora);
        console.log(duracion);
        console.log("Dni Doctor:" + dni_doctor[0].DNIDoctor);


        const fechaHoraString = fecha + ' ' + hora + ':00'; // Combinar fecha y hora en una sola cadena, en formato ISO 8601 y UTC
        const nuevoDate = new Date(fechaHoraString);
        nuevoDate.setHours(nuevoDate.getHours() - 2);
        nuevoDate.setMonth(nuevoDate.getMonth() - 2);

        console.log(nuevoDate);
        // Convertir la fecha ajustada a una cadena en formato ISO 8601
        const fecha_hora = formatISODateWithoutAdjustment(nuevoDate);

        console.log(fechaHoraString);
        console.log(fecha_hora);


        await dao.crearNotificacionCita(tipo,motivo,fechaHoraString,dni_doctor[0].DNIDoctor,dni_paciente);


        res.json(true)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});

function formatISODateWithoutAdjustment(date) {
    // Obtener partes de la fecha
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    const milliseconds = String(date.getMilliseconds()).padStart(3, '0');
  
    // Formatear la fecha sin ajustar a UTC
    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}.${milliseconds}`;
  }



router.get('/obtenerAlarmas/:dni', async function(req, res, next) {

    try {
        
        var dni_paciente=req.params.dni;

        var paciente = await dao.obtenerAlarmas(dni_paciente)

        console.log(paciente)
        res.json(paciente)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
      }
   
});

router.get('/obtenerCitasPaciente/:dni', async function(req, res, next) {

    try {
        
        var dni_paciente=req.params.dni;

        var paciente = await dao.obtenerCitasPaciente(dni_paciente);

        console.log(paciente)
        res.json(paciente)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
      }
   
});


router.get('/obtenerMensajesNoLeidos/:dni', async function(req, res, next) {

    try {
        
        var dni_paciente=req.params.dni;
        //console.log(dni_paciente)

        var mensajes = await dao.obtenerMensajesNoLeidos(dni_paciente);

        //console.log(mensajes)
        res.json(mensajes)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
      }
   
});


router.get('/obtenerDoctoresDelPaciente/:dni', async function(req, res, next) {

    try {
        
        var dni_paciente=req.params.dni;

        var dniDoctores = await dao.obtenerDoctorDelPaciente(dni_paciente);

        console.log(dniDoctores)

        var doctores = [];

        for (let i = 0; i < dniDoctores.length; i++) {
            let dniDoctor = dniDoctores[i].DNIDoctor;
            console.log(dniDoctor)
            let nombreDoctor = await dao.obtenerInformacionDoctor(dniDoctor); // Supongamos que esta función existe en tu DAO
            doctores.push(nombreDoctor[0]);
        }

        console.log(doctores)
    
        res.json(doctores)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});


router.get('/obtenerPaciente/:dni', async function(req, res, next) {

    try {
        
        var dni=req.params.dni;

        var paciente = await dao.obtenerPaciente(dni);

        console.log(paciente)
        res.json(paciente)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});

var consultaRouter = require('./consultaP');
router.use('/consulta', consultaRouter);

var usuarioRouter = require('./usuarioP');
router.use('/usuario', usuarioRouter);


module.exports = router;