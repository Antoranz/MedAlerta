var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/registrar', function(req, res, next) {
  res.render("registrar",{error:""});
});
const mysql = require('mysql');

// Configuración de la conexión a la base de datos MySQL en XAMPP
const conexion = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'medalerta'
});

// Establecer la conexión
conexion.connect((error) => {
    if (error) {
        console.error('Error al conectar a la base de datos: ' + error.message);
    } else {
        console.log('Conexión a la base de datos exitosa');
    }
});
router.get('/obtener-doctores', (req, res) => {
  const sql = 'SELECT * FROM doctores';

  conexion.query(sql, (err, result) => {
      if (err) {
          console.error('Error al obtener doctores: ' + err.message);
          res.status(500).send('Error interno del servidor');
      } else {
          res.json(result);
      }
  });
});
router.post('/registrando', function(req, res, next) {
  const {dni,email,password,nombre,apellidos,fecha_nacimiento,domicilio,codigo_postal,numero_telefono} = req.body;

  // Sentencia SQL para insertar un nuevo doctor
  const sql = `INSERT INTO doctores 
              (dni, email, password, nombre, apellidos, fecha_nacimiento, domicilio, codigo_postal, numero_telefono)
              VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`;

  // Parámetros para la sentencia SQL
  const params = [dni,email,password,nombre,apellidos,fecha_nacimiento,domicilio,codigo_postal,numero_telefono];
  conexion.query(sql, params, (error, resultados) => {
    if(error){
      console.log("Error interno del servidor");
    }else{
      console.log(resultados);
    }
  });
  console.log(req.body);
});
module.exports = router;
