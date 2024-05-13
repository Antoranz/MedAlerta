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

        const {doctor_dni,tipo,motivo,fecha,hora} = req.body;

        console.log(tipo);
        console.log(motivo);
        console.log(fecha);
        console.log(hora);
        console.log(doctor_dni);

        const fechaHoraString = fecha + ' ' + hora + ':00'; // Combinar fecha y hora en una sola cadena, en formato ISO 8601 y UTC
        
       

        console.log(fechaHoraString);
       


        await dao.crearNotificacionCita(tipo,motivo,fechaHoraString,doctor_dni,dni_paciente);


        res.json(true)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});


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

router.get('/comprobarDniExistente/:dni', async function(req, res, next) {

    try {
        console.log("COMPROBAR DNI EXISTENTE")
        var dni_paciente=req.params.dni;

        var paciente = await dao.comprobarDniExistente(dni_paciente);

        console.log(dni_paciente)
        console.log(paciente)
        res.json(paciente)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
      }
   
});

router.get('/comprobarEmailExistente/:email', async function(req, res, next) {

    try {
        console.log("COMPROBAR DNI EXISTENTE")
        var email_paciente=req.params.email;

        var paciente = await dao.comprobarEmailExistente(email_paciente);

        console.log(email_paciente)
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

        var citas = await dao.obtenerCitasPaciente(dni_paciente);

        console.log(citas)
        res.json(citas)
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