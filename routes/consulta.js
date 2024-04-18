var express = require('express');
var router = express.Router();
const DAODoctor = require("../DAODoctores")


const mysql = require('mysql');
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'medalerta'
});

const dao = new DAODoctor(pool);

router.get('/', async function(req, res, next) {
    if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
      res.render('index', { nombre:"" });
    }else{
  
      var consultas = await dao.obtenerConsultas_doctor(req.session.currentUser.dni);
  
      res.render('consultas', { nombre: req.session.currentUser.nombre, listaConsultas: consultas });
    }
});

router.get('/mensajes/:id', async function(req, res, next) {
    if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
        res.render('index', { nombre: "" });
    } else {
        try {
            var mensajes = await dao.obtenerMensajes_consulta(req.params.id);
  
            res.json({ mensajes: mensajes });
        } catch (error) {
  
            res.status(500).json({ error: error.message });
        }
    }
  });

router.post("/mandarMensaje", async function(req, res, next) {
    if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
      res.render('index', { nombre:"" });
    }else{
      try{
        const fecha = new Date();
  
        var mensaje = await dao.mandarMensaje(req.body.consultaId ,req.body.texto, 0, fecha );
  
        res.json({mensaje: mensaje});
      } catch(error){
        res.status(500).json({ error: error.message });
      }
    }
  });

  module.exports = router;
