  var express = require('express');
  var router = express.Router();
  const crypto = require('crypto');
  const DAODoctor = require("../DAODoctores")


const mysql = require('mysql');
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'medalerta'
});

const dao = new DAODoctor(pool);

router.post('/registrando', registrarUsuario);

function registrarUsuario(req, res, next) {
  const {dni,email,password,repeatPassword,nombre,apellidos,fecha_nacimiento,domicilio,codigo_postal,numero_telefono} = req.body;
  var usuario = {dni: dni,email: email,password: password,repeatPassword: repeatPassword,nombre: nombre,apellidos: apellidos,fecha_nacimiento: fecha_nacimiento,domicilio: domicilio,codigo_postal: codigo_postal,numero_telefono: numero_telefono};

  var valido = true;
  if (password !== repeatPassword) {
    valido = false;
    usuario.password = "";
    usuario.repeatPassword = "";
    res.render("register",{error:"Las contraseñas no coinciden", usuario: usuario,email:"",nombre: ""});
  }

  if (!/^\d{8}[a-zA-Z]$/.test(dni)) {
    valido = false;
    usuario.dni = "";
    res.render("register",{error:"El DNI no es válido", usuario: usuario,email:"",nombre: ""});
  }

  const fechaNacimiento = new Date(fecha_nacimiento);
  const fechaActual = new Date();
  if (fechaNacimiento >= fechaActual) {
    valido = false;
    usuario.fecha_nacimiento = "";
    res.render("register",{error:"La fecha de nacimiento debe ser anterior a la fecha actual", usuario: usuario,email:"",nombre: ""});
  }

  if (!/^\d{9}$/.test(numero_telefono)) {
    valido = false;
    usuario.numero_telefono = "";
    res.render("register",{error:"El número de teléfono no es válido", usuario: usuario,email:"",nombre: ""});
  }

  if(valido){
    var hashedPassword = cifrarContrasena(password,dni + "caminar es bueno para la salud");

    dao.aniadirDoctor(dni, email, hashedPassword, nombre, apellidos, fecha_nacimiento, domicilio, codigo_postal, numero_telefono)
    .then((resultado) => {
      req.session.currentUser = {dni,email,nombre,apellidos};

      res.render("index",{nombre : ""});
    })
    .catch((error) => {
        console.error("Error interno del servidor" + error);
    });
  }

};

var hashedPassword = cifrarContrasena("hola123", "08366085L" + "caminar es bueno para la salud");
console.log(hashedPassword);
router.post('/signin' , async function(req,res,next){

    try {
  
      var dni=req.body.dni;
      var password=req.body.password;

      console.log(password)
  
      var hashedPassword = cifrarContrasena(password,dni);
  
      console.log("Contraseña inicio de sesion: " + hashedPassword);
  
      var doctor = await dao.checkDoctor(dni,hashedPassword);
  
      if(doctor.length===0){
        res.render('index', {error: 'Las credenciales son incorrectas', confirmacion: '',nombre:""});
  
      }else{
  
        doctor[0].validado = true;
        req.session.currentUser = doctor[0]
        delete req.session.currentUser.password;
        req.session.currentUser.fecha_nacimiento = new Date(req.session.currentUser.fecha_nacimiento);
        req.session.currentUser.fecha_nacimiento.setDate(req.session.currentUser.fecha_nacimiento.getDate() + 1);//por alguna razon se resta un día a lo que hay en la bbdd
        req.session.currentUser.fecha_nacimiento =obtenerFecha(req.session.currentUser.fecha_nacimiento);
          
        console.log(doctor[0])
        console.log(req.session.currentUser)
        res.redirect('/');
      }
      
      
    } catch (error) {
      console.error("Error durante la operación:", error);
    }
  
  });

  router.get('/editar-perfil', function(req, res, next) {
    if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
      res.render('index', { nombre:"" });
    }else{
      usuario = req.session.currentUser;
      console.log(usuario);
      res.render("editarPerfil",{error:"", usuario: usuario,nombre: req.session.currentUser.nombre});
    }
  });

  router.get('/register', function(req, res, next) {
    res.render("register",{error:"", usuario: "",nombre: ""});
  });

  function obtenerFecha(cadenaFechaHora) {
    if (typeof cadenaFechaHora === 'string') {
      var soloFecha = cadenaFechaHora.split("T")[0];
      return soloFecha;
    } else if (cadenaFechaHora instanceof Date) {
      var soloFecha = cadenaFechaHora.toISOString().split("T")[0];
      return soloFecha;
    }else {
        return ""; 
    }
  }

  function cifrarContrasena(contrasena, salt) {
    // Crea un nuevo objeto Hash
    const hash = crypto.createHash('sha256');
  
    // Actualiza el hash con la contraseña y el salt
    hash.update(contrasena + salt);
  
    // Devuelve el hash en formato hexadecimal
    return hash.digest('hex');
  }


module.exports = router;