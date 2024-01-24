var express = require('express');
var router = express.Router();
const DAOPaciente = require("../DAOPacientes")

/* GET home page. */
const mysql = require('mysql');

// Configuración de la conexión a la base de datos MySQL en XAMPP
const pool = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'medalerta'
});


const dao = new DAOPaciente(pool);

router.post('/validarPaciente', async function(req, res, next) {

    console.log(req.body.codigo);
    res.json(true)

   
});

router.post('/bajaPaciente', async function(req, res, next) {

    try {
        await dao.bajaPaciente(req.body.dniPaciente);
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

        var paciente = await dao.checkPaciente(email,password);

        console.log(paciente)
        res.json(paciente)
    } catch (error) {
        console.error("Error durante la operación:", error);
        res.json(null)
        
      }
   
});

module.exports = router;
