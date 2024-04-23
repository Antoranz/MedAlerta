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