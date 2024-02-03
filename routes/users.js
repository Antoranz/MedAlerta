var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/register', function(req, res, next) {
  res.render("register",{error:""});
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
  var hashedPassword = cifrarContrasena(password,dni);
  // Sentencia SQL para insertar un nuevo doctor
  const sql = `INSERT INTO doctores 
              (dni, email, password, nombre, apellidos, fecha_nacimiento, domicilio, codigo_postal, numero_telefono)
              VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`;

  // Parámetros para la sentencia SQL
  const params = [dni,email,hashedPassword,nombre,apellidos,fecha_nacimiento,domicilio,codigo_postal,numero_telefono];
  conexion.query(sql, params, (error, resultados) => {
    if(error){
      console.log("Error interno del servidor" + error);
    }else{
      res.render('gestionUsuarios',{email : req.session.currentUser.email})
    }
  });
  console.log(req.body);
});

const crypto = require('crypto');

function cifrarContrasena(contrasena, salt) {
  // Crea un nuevo objeto Hash
  const hash = crypto.createHash('sha256');

  // Actualiza el hash con la contraseña y el salt
  hash.update(contrasena + salt);

  // Devuelve el hash en formato hexadecimal
  return hash.digest('hex');
}

module.exports = router;
