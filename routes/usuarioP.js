var express = require('express');
var router = express.Router();
const nodemailer = require("nodemailer");
const DAOPaciente = require("../DAOPacientes");
const {sendVerificationEmail} = require('../mailer.js');

const crypto = require('crypto');

const mysql = require('mysql');

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
        var hashedPassword = cifrarContrasena(password,DNI + "caminar es bueno para la salud");

        console.log("FECHAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        console.log(hashedPassword)
        console.log(DNI)
        console.log(password)

        await dao.registrarPaciente(Nombre,Apellidos,FechaDeNacimiento,Direccion,CodigoPostal,telefono,email,DNI,hashedPassword);

        res.json(true)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
    }
});

router.post('/checkPaciente', async function(req, res, next) {

    try {

        var dni=req.body.dni;
        var password=req.body.password;

        var hashedPassword = cifrarContrasena(password,dni + "caminar es bueno para la salud");

        console.log(hashedPassword)
        console.log(dni)
        console.log(password)

        var paciente = await dao.checkPaciente(dni,hashedPassword);

        console.log(dni)
        console.log(password)
        console.log(paciente)
        res.json(paciente)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});

function cifrarContrasena(contrasena, salt) {
    const hash = crypto.createHash('sha256');
  
    hash.update(contrasena + salt);
  
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

        const {password,dni} = req.body;

        var hashedPassword = cifrarContrasena(password,dni + "caminar es bueno para la salud");

        await dao.editarPassword(hashedPassword,dni);

        res.json(true)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});

module.exports = router;