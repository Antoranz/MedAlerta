
var express = require('express');
var router = express.Router();
const DAODoctor = require("../DAODoctores")
const DAOPaciente = require("../DAOPacientes")
const PDFDocument = require('pdfkit');
const fs = require('fs');
const crypto = require('crypto');


const mysql = require('mysql');

// Configuración de la conexión a la base de datos MySQL en XAMPP
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'medalerta'
});


const dao = new DAODoctor(pool);
const dao2 = new DAOPaciente(pool);


/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.post('/signin' , async function(req,res,next){

  try {

    var dni=req.body.dni;
    var password=req.body.password;

    var hashedPassword = cifrarContrasena(password,dni);

    console.log("Contraseña inicio de sesion: " + hashedPassword);

    var doctor = await dao.checkDoctor(dni,hashedPassword);

    if(doctor.length===0){
      console.log("aaaaaaaaaaaa "+doctor);
      res.render('index', {error: 'Las credenciales son incorrectas', confirmacion: ''});

    }else{

      doctor[0].validado = true;
      req.session.currentUser = doctor[0]
      console.log(doctor[0])
      console.log(req.session.currentUser)
      res.render('gestionUsuarios',{email : req.session.currentUser.email});
    }
    
    
} catch (error) {
    console.error("Error durante la operación:", error);
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

router.get('/register', function(req, res, next) {
  res.render("register",{error:""});
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

function cifrarContrasena(contrasena, salt) {
  // Crea un nuevo objeto Hash
  const hash = crypto.createHash('sha256');

  // Actualiza el hash con la contraseña y el salt
  hash.update(contrasena + salt);

  // Devuelve el hash en formato hexadecimal
  return hash.digest('hex');
}

router.post("/aniadirUsuario", async function(req, res, next) {
  
  const usuarioP = req.body.dni;

  if(usuarioP.includes("@")){
    
    var paciente = await dao2.obtenerPaciente_email(usuarioP);

    
    if(paciente.length!==0){

      dao.aniadirUsuario(req.session.currentUser.dni, paciente[0].dni);

    }

  }else{

    var paciente = await dao2.obtenerPaciente(usuarioP);
  
    if(paciente.length !==0){
      dao.aniadirUsuario(req.session.currentUser.dni,usuarioP)
    }
   
  }

  res.render('gestionUsuarios',{email : req.session.currentUser.email});

});

router.get('/getUsuarios/', async function(req, res, next) {
  try {

    var pacientes = await dao.obtenerPacientes_doctor(req.session.currentUser.dni)

    console.log(pacientes)

    res.json(pacientes);
  } catch (error) {
    console.error("Error al obtener usuarios:", error);
    res.status(500).json({ error: "Error al obtener usuarios" });
  }
});

router.get('/historial', function(req, res, next) {
    res.render('historialMedico', { title: 'Express' ,userData: ""});
  });
  
  router.post('/guardarHistorial', function(req, res){
    // Creamos un nuevo documento PDF
    const doc = new PDFDocument();

    // Creamos el contenido del PDF con los datos del formulario
    doc.text(`Nombre: ${req.body.nombre}`);
    doc.text(`Apellido: ${req.body.apellido}`);
    doc.text(`Sexo: ${req.body.sexo}`);
    doc.text(`Fecha de Nacimiento: ${req.body.fechaNacimiento}`);
    doc.text(`Edad: ${req.body.edad}`);
    doc.text(`Peso (kg): ${req.body.peso}`);
    doc.text(`Altura (cm): ${req.body.altura}`);
    doc.text(`Alergias: ${req.body.alergias}`);
    doc.text(`Notas: ${req.body.notas}`);

    // Creamos un flujo de escritura para guardar el PDF en un archivo local
    const filePath = "../MedAlerta/public/historiales/historial_medico.pdf"
    const writeStream = fs.createWriteStream(filePath);

    // Manejamos eventos de error y finalización del flujo de escritura
    writeStream.on('finish', () => {
        res.send('PDF guardado en ./historiales/historial_medico.pdf');
    });

    writeStream.on('error', (err) => {
        console.error('Error al guardar el PDF:', err);
        res.status(500).send('Error al guardar el PDF.');
    });

    // Piping PDFKit to writeStream
    doc.pipe(writeStream); 

    // Finalizamos el documento PDF
    doc.end();
});

module.exports = router;
