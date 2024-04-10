var express = require('express');
var router = express.Router();
const nodemailer = require("nodemailer");
const DAOPaciente = require("../DAOPacientes")
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

router.post('/validarPaciente', async function(req, res, next) {
    console.log(req.body.email);
    console.log(req.body.codigo);

    sendVerificationEmail(req.body.email,req.body.codigo)

    res.json(true)
});



router.post('/registrarPaciente', async function(req, res, next) {

    const {Nombre,Apellidos,FechaDeNacimiento,Direccion,CodigoPostal,telefono,email,DNI,password} = req.body;

    try {
        var hashedPassword = cifrarContrasena(password,email);

        console.log("FECHAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        console.log(FechaDeNacimiento)

        await dao.registrarPaciente(Nombre,Apellidos,FechaDeNacimiento,Direccion,CodigoPostal,telefono,email,DNI,hashedPassword);

        res.json(true)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
    }
});

router.post('/crearConsulta', async function(req, res, next) {

    const {Nombre,titulo,fecha} = req.body;

    try {
        
        console.log(Nombre)
        console.log(titulo)
        console.log(fecha)

        //await dao.registrarPaciente(Nombre,Apellidos,FechaDeNacimiento,Direccion,CodigoPostal,telefono,email,DNI,hashedPassword);

        res.json(true)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
    }
});

function cifrarContrasena(contrasena, salt) {
    // Crea un nuevo objeto Hash
    const hash = crypto.createHash('sha256');
  
    // Actualiza el hash con la contraseña y el salt
    hash.update(contrasena + salt);
  
    // Devuelve el hash en formato hexadecimal
    return hash.digest('hex');
}

router.post('/bajaPaciente', async function(req, res, next) {

    try {


        await dao.bajaPaciente_alarmas(req.body.dniPaciente)
        await dao.bajaPaciente_tratamientos(req.body.dniPaciente)
        await dao.bajaPaciente(req.body.dniPaciente);
        res.json(true)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});



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


        const fechaHoraString = fecha + ' ' + hora; // Combinar fecha y hora en una sola cadena
        const nuevoDate = new Date(fechaHoraString);

        // Sumar una hora a la fecha
        nuevoDate.setHours(nuevoDate.getHours() + 1);

        // Convertir la fecha ajustada a una cadena en formato ISO
        const fecha_hora = nuevoDate.toISOString();

        console.log(fechaHoraString);
        console.log(fecha_hora);


        await dao.crearNotificacionCita(tipo,motivo,fecha_hora,dni_doctor[0].DNIDoctor,dni_paciente);


        res.json(true)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});

router.post('/checkPaciente', async function(req, res, next) {

    try {

        var email=req.body.email;
        var password=req.body.password;

        var hashedPassword = cifrarContrasena(password,email);

        console.log(hashedPassword)

        var paciente = await dao.checkPaciente(email,hashedPassword);

        console.log(email)
        console.log(password)
        console.log(paciente)
        res.json(paciente)
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

router.get('/obtenerConsultasPaciente/:dni', async function(req, res, next) {

    try {
        
        var dni_paciente=req.params.dni;

        var consultas = await dao.obtenerConsultasPaciente(dni_paciente)

        console.log(consultas)
        res.json(consultas)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});

router.get('/obtenerMensajesConsulta/:idConsulta', async function(req, res, next) {

    try {
        
        var id_consulta=req.params.idConsulta;

        var mensajes = await dao.obtenerMensajesConsulta(id_consulta)

        console.log(mensajes)
        res.json(mensajes)
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

router.post('/editarPaciente/:dni', async function(req, res, next) {

    try {

        var dni=req.params.dni;

        const {Nombre,Apellidos,FechaDeNacimiento,Direccion,CodigoPostal,telefono} = req.body;


        await dao.editarPaciente(Nombre,Apellidos,FechaDeNacimiento,Direccion,CodigoPostal,telefono,dni);

        res.json(true)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});


router.post('/editarPassword', async function(req, res, next) {

    try {

        const {password,email} = req.body;

        var hashedPassword = cifrarContrasena(password,email);

        console.log(hashedPassword)

        console.log(password)
        console.log(email)

        await dao.editarPassword(hashedPassword,email);

        res.json(true)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});



module.exports = router;
