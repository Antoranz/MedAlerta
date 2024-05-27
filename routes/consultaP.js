var express = require('express');
var router = express.Router();
const DAOPaciente = require("../DAOPacientes");
const {sendVerificationEmail} = require('../mailer.js');

const mysql = require('mysql');

const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'medalerta'
});

const dao = new DAOPaciente(pool);

router.post('/crearConsulta', async function(req, res, next) {

    const {dni_doctor,dni_paciente,titulo,fecha} = req.body;

    try {

        const fechaDate = new Date(fecha);

        const consulta = await dao.registrarConsulta(dni_doctor,dni_paciente,titulo,fechaDate);
        
        const consultaId = consulta.insertId;

        const mensaje = "Hola, soy tu doctor, dime lo que necesites"
        const propietario = 0
        const leido_paciente = 1

        await dao.registrarMensajeInicialDoctor(consultaId,mensaje,propietario,fechaDate,leido_paciente);

        res.json(true)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
    }
});

router.post('/crearMensaje', async function(req, res, next) {

    const {id_consulta,mensaje,propietario,fecha} = req.body;

    try {

        const fechaDate = new Date(fecha);

        
        await dao.registrarMensaje(id_consulta,mensaje,propietario,fechaDate);
        await dao.updateNotificacionDoctor(id_consulta);



        res.json(true)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
    }
});

router.get('/saberSiHayMensajesNoLeidos/:id/:dni', async function(req, res, next) {
    try {
        var id_consulta = req.params.id;
        var dni_paciente = req.params.dni;

        var mensajes = await dao.obtenerMensajesNoLeidos(dni_paciente);

        var hayMensajesNoLeidos = false;

        for (var i = 0; i < mensajes.length; i++) {
            console.log(mensajes[i].id_consulta)
            console.log(parseInt(id_consulta))
            
            if (mensajes[i].id_consulta === parseInt(id_consulta)) {
                
                console.log("ha detectado un mensaje coincidente")
                hayMensajesNoLeidos = true;
                break;
            }
        }

        
        res.json({"leidos":hayMensajesNoLeidos});
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null);
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

        res.json(mensajes)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});

router.post('/ponerMensajesComoLeidosConsulta/:id', async function(req, res, next) {

    try {

        var id_consulta=req.params.id;
        console.log(id_consulta)

        var paciente = await dao.ponerMensajesComoLeidosConsulta(id_consulta);

        console.log(paciente);
        

        res.json(true)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});

router.get('/obtenerMensajesNoLeidos/:dni', async function(req, res, next) {

    try {
        
        var dni_paciente=req.params.dni;

        var mensajes = await dao.obtenerMensajesNoLeidos(dni_paciente);

        console.log(mensajes)

        res.json(mensajes)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
      }
   
});

module.exports = router;