
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
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('index', { nombre:"" });
  }else{
    res.render('main', { nombre: req.session.currentUser.nombre });
  }
});
router.get('/calendario', function(req, res, next) {
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('index', { nombre:"" });
  }else{
    res.render('calendario', { nombre: req.session.currentUser.nombre });
  }
});
router.get("/obtener-citas",async function(req, res,next){
  var citas = await dao.checkCitas(req.session.currentUser.dni);
  var eventos=[];
  console.log(citas);
  citas.forEach(function(c){
    const fechaHoraObj = new Date(c.fecha_hora); // Crear objeto Date directamente desde c.fecha_hora
    const nuevaFechaHora = new Date(fechaHoraObj.getTime() + c.duracion * 60000); // Sumar duración en milisegundos
    eventos.push({
      "id":c.id,
      "title": `Cita médica con ${c.nombre_paciente} ${c.apellidos_paciente}`,
      "start": c.fecha_hora,
      "end": nuevaFechaHora
    });
  });
  res.json(eventos);
});
router.post("/eliminar-cita",async function(req, res,next){
  const citaId = req.body.id;
    try {
        // Aquí realizas la lógica para eliminar la cita de la base de datos
        await dao.eliminarCita(citaId);
        // Si la eliminación fue exitosa, envía una respuesta con el código de estado 200 (Éxito)
        res.status(200).json({ message: 'La cita se eliminó correctamente.' });
    } catch (error) {
        // Si ocurrió algún error durante la eliminación, envía una respuesta con el código de estado 500 (Error del servidor)
        res.status(500).json({ error: 'Se produjo un error al intentar eliminar la cita.' });
    }
});

router.post("/asignar-cita", async function(req, res, next) {
  const dni = req.body.dni;
  const fecha = req.body.fecha;
  const hora = req.body.hora;
  const duracion = req.body.duracion;
  const fechaHoraString = fecha + ' ' + hora; // Combinar fecha y hora en una sola cadena
  var nuevoDate = new Date(fechaHoraString);
  const fechaActual = new Date();

  if (nuevoDate == "Invalid Date") {

    const fechaParts = fecha.split('/'); // Divide la cadena de fecha por "/"
    const horaParts = hora.split(':'); // Divide la cadena de hora por ":"

    // Crea un nuevo objeto Date con los componentes de fecha y hora
    const nuevaFechaaaaaaaa = new Date(
        fechaParts[2], // Año
        parseInt(fechaParts[1]) - 1, // Mes (se resta 1 porque los meses en JavaScript van de 0 a 11)
        fechaParts[0], // Día
        horaParts[0], // Hora
        horaParts[1], // Minuto
        horaParts[2] // Segundo
    );

    console.log("Ha entrado en invalid");

    nuevoDate = nuevaFechaaaaaaaa;
    console.log(nuevaFechaaaaaaaa)

    
  }


  console.log(dni)
  console.log(fecha)
  console.log(hora)
  console.log(duracion)
  console.log(nuevoDate)

  try {
    if (!/^\d{8}[a-zA-Z]$/.test(dni)) {
      throw new Error("Formato de DNI inválido");
    }

    const paciente = await dao2.obtenerPaciente(dni);

    if (paciente.length === 0) {
      throw new Error("El paciente no existe");
    }

    if (duracion <= 0 || duracion > 60) {
      throw new Error("La duración de la cita debe estar entre 1 y 60 minutos");
    }

    if (nuevoDate <= fechaActual) {
      throw new Error("La fecha y hora seleccionadas deben ser posteriores a la fecha y hora actual");
    }
    const fecha_hora = nuevoDate.toISOString();
    var citasCoincidentes = await dao.checkearCitasCoincidentes(req.session.currentUser.dni, fecha_hora, duracion);
    console.log("citas:", citasCoincidentes);
    if (citasCoincidentes.length > 0) {
      throw new Error("Ya existe una cita en ese intervalo de tiempo");
    }
    
    await dao.asignarCita(req.session.currentUser.dni, dni, nuevoDate, duracion);
    
    res.status(200).send("Cita asignada correctamente");
  } catch (error) {
    console.error("Error al asignar cita:", error.message);
    res.status(400).send(error.message);
  }
});

router.get('/gestion-usuarios', function(req, res, next) {
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('index', { nombre:"" });
  }else{
    res.render('gestionUsuarios', { nombre: req.session.currentUser.nombre });
  }
});



router.get('/gestion-notificaciones', async function(req, res, next) {
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('index', { nombre:"" });
  }else{

    var notificaciones = await dao.obtenerNotificaciones(req.session.currentUser.dni);

    

    notificaciones.forEach(notificacion => {
      const fechaHora = new Date(notificacion.fecha_hora);
      // Asignar la fecha y la hora por separado a la notificación
      notificacion.fecha = fechaHora.toLocaleDateString();
      notificacion.hora = fechaHora.toLocaleTimeString();

      notificacion.id = notificacion.id;

  });

  console.log(notificaciones)
  

    res.render('gestionNotificaciones', { nombre: req.session.currentUser.nombre, notificaciones:notificaciones });
  }
});


router.post('/signin' , async function(req,res,next){

  try {

    var dni=req.body.dni;
    var password=req.body.password;

    var hashedPassword = cifrarContrasena(password,dni);

    console.log("Contraseña inicio de sesion: " + hashedPassword);

    var doctor = await dao.checkDoctor(dni,hashedPassword);

    if(doctor.length===0){
      res.render('index', {error: 'Las credenciales son incorrectas', confirmacion: '',nombre:""});

    }else{

      doctor[0].validado = true;
      req.session.currentUser = doctor[0]
      console.log(doctor[0])
      console.log(req.session.currentUser)
      res.redirect('/');
    }
    
    
  } catch (error) {
    console.error("Error durante la operación:", error);
  }

});
router.get('/funciones-paciente/:dni', (req, res) => {
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('index', { nombre:"" });
  }else{
    const dniUsuario = req.params.dni;
    res.render("funcionesUsuario",{ nombre:req.session.currentUser.nombre,dni:dniUsuario});
  }
});
router.get('/obtener-doctores', (req, res) => {
  const sql = 'SELECT * FROM doctores';

  pool.query(sql, (err, result) => {
      if (err) {
          console.error('Error al obtener doctores: ' + err.message);
          res.status(500).send('Error interno del servidor');
      } else {
          res.json(result);
      }
  });
});

router.get('/register', function(req, res, next) {
  res.render("register",{error:"", usuario: "",email:""});
});


router.post('/registrando', function(req, res, next) {
  const {dni,email,password,repeatPassword,nombre,apellidos,fecha_nacimiento,domicilio,codigo_postal,numero_telefono} = req.body;
  var usuario = {dni: dni,email: email,password: password,repeatPassword: repeatPassword,nombre: nombre,apellidos: apellidos,fecha_nacimiento: fecha_nacimiento,domicilio: domicilio,codigo_postal: codigo_postal,numero_telefono: numero_telefono};
  console.log(usuario);
  var valido = true;
  if (password !== repeatPassword) {
    valido = false;
    usuario.password = "";
    usuario.repeatPassword = "";
    res.render("register",{error:"Las contraseñas no coinciden", usuario: usuario,email:""});
  }

  if (!/^\d{8}[a-zA-Z]$/.test(dni)) {
    valido = false;
    usuario.dni = "";
    res.render("register",{error:"El DNI no es válido", usuario: usuario,email:""});
  }

  const fechaNacimiento = new Date(fecha_nacimiento);
  const fechaActual = new Date();
  if (fechaNacimiento >= fechaActual) {
    valido = false;
    usuario.fecha_nacimiento = "";
    res.render("register",{error:"La fecha de nacimiento debe ser anterior a la fecha actual", usuario: usuario,email:""});
  }

  if (!/^\d{9}$/.test(numero_telefono)) {
    valido = false;
    usuario.numero_telefono = "";
    res.render("register",{error:"El número de teléfono no es válido", usuario: usuario,email:""});
  }

  if(valido){
    var hashedPassword = cifrarContrasena(password,dni);
    // Sentencia SQL para insertar un nuevo doctor
    const sql = `INSERT INTO doctores 
                (dni, email, password, nombre, apellidos, fecha_nacimiento, domicilio, codigo_postal, numero_telefono)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`;
  
    // Parámetros para la sentencia SQL
    const params = [dni,email,hashedPassword,nombre,apellidos,fecha_nacimiento,domicilio,codigo_postal,numero_telefono];
    pool.query(sql, params, (error, resultados) => {
      if(error){
        console.log("Error interno del servidor" + error);
      }else{
        req.session.currentUser = {dni,email,nombre,apellidos};
        res.render('index',{email : ""})
      }});
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

router.post("/aniadirUsuario", async function(req, res, next) {
  
  const usuarioP = req.body.dni;

  if(usuarioP.includes("@")){ //
    
    var paciente = await dao2.obtenerPaciente_email(usuarioP);
    
    if(paciente.length!==0){
      console.log("aniado paciente con dni:" + paciente[0].dni + " al doctor con dni: "+ req.session.currentUser.dni);
      dao.aniadirUsuario(req.session.currentUser.dni, paciente[0].dni);

    }

  }else{

    var paciente = await dao2.obtenerPaciente(usuarioP);
    if(paciente.length !==0){
      console.log("aniado paciente con dni:" + paciente[0].dni + " al doctor con dni: "+ req.session.currentUser.dni);
      dao.aniadirUsuario(req.session.currentUser.dni,usuarioP)
    }
   
  }

  res.render('gestionUsuarios',{nombre : req.session.currentUser.nombre});

});

router.post("/guardarTratamiento", async function(req, res, next) {

  
  const dniPaciente = req.body.dniPaciente;
  const descripcion = req.body.descripcion;
  

  const result = await dao.guardarTratamiento(req.session.currentUser.dni,dniPaciente,descripcion)

  
  console.log(dniPaciente);
  console.log(result.insertId)


   // varios meidcamentos en forma de array
   if (Array.isArray(req.body['medicamento[]'])) {
    for (let i = 0; i < req.body['medicamento[]'].length; i++) {
        const medicamento = req.body['medicamento[]'][i];
        const dosis = req.body['dosis[]'][i];
        const horaPrimeraToma = req.body['hora_primera_toma[]'][i];
        const tomasAlDia = req.body['tomas_al_dia[]'][i];
        const fechaInicio = req.body['fecha_inicio[]'][i];
        const fechaFin = req.body['fecha_fin[]'][i];

        console.log(medicamento);
        console.log(dosis);
        console.log(horaPrimeraToma);
        console.log(tomasAlDia);
        console.log(fechaInicio);
        console.log(fechaFin);

        await dao.guardarAlarma(result.insertId, medicamento, dosis, horaPrimeraToma, tomasAlDia, fechaInicio, fechaFin);
    }
} else { //Sólo un medicamento
    const medicamento = req.body['medicamento[]'];
    const dosis = req.body['dosis[]'];
    const horaPrimeraToma = req.body['hora_primera_toma[]'];
    const tomasAlDia = req.body['tomas_al_dia[]'];
    const fechaInicio = req.body['fecha_inicio[]'];
    const fechaFin = req.body['fecha_fin[]'];

    console.log(medicamento);
    console.log(dosis);
    console.log(horaPrimeraToma);
    console.log(tomasAlDia);
    console.log(fechaInicio);
    console.log(fechaFin);

    await dao.guardarAlarma(result.insertId, medicamento, dosis, horaPrimeraToma, tomasAlDia, fechaInicio, fechaFin);
}

  res.render('gestionUsuarios',{nombre : req.session.currentUser.nombre});

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

router.get('/eliminarAsociacion/:dni', async function(req, res, next) {
  try {

    const dniUsuario = req.params.dni;

    console.log(dniUsuario)


    dao.eliminarAsociacion(req.session.currentUser.dni,dniUsuario)


    res.render('gestionUsuarios',{nombre : req.session.currentUser.nombre});



  } catch (error) {
    
    console.error("Error al obtener usuarios:", error);
  }
});

router.get('/CrearHistorial/', function(req, res, next) {
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('index', { nombre:"" });
  }else{
    res.render('historialMedico', { title: 'Express' ,userData: "",nombre:req.session.currentUser.nombre});
  }
});
  
  router.post('/guardarHistorial', function(req, res){
    // Creamos un nuevo documento PDF
    const doc = new PDFDocument();
    
    
    function obtenerFechaActual() {
      const fecha = new Date();
      const dia = fecha.getDate();
      const mes = fecha.getMonth() + 1;
      const anio = fecha.getFullYear();
      return `${dia}/${mes}/${anio}`;
    }

    // Definir coordenadas y dimensiones del rectángulo del título
    const tituloRectX = 50;
    const tituloRectY = 50;
    const tituloRectWidth = 500;
    const tituloRectHeight = 80;
    const tituloRectBorderWidth = 2;

    // Dibujar el rectángulo del título con borde negro y fondo gris
    doc.rect(tituloRectX, tituloRectY, tituloRectWidth, tituloRectHeight)
        .lineWidth(tituloRectBorderWidth)
        .fillColor('#CCCCCC') // Color de fondo gris
        .strokeColor('#000000') // Color de borde negro
        .stroke()
        .fill();

    // Título del documento
    doc.font('Helvetica-Bold').fontSize(24)
        .fillColor('#000000') // Cambiar color de texto a negro
        .text('Historial Médico', { align: 'center', lineGap: 10 });

    // Fecha actual
    doc.font('Helvetica').fontSize(16)
        .text(obtenerFechaActual(), { align: 'center' });

    // Ruta de la imagen a añadir
    const rutaImagen = './public/images/logoMedAlerta.png';

    // Coordenadas de la imagen en el PDF
    const imagenX = 70;
    const imagenY = 50;
    const imagenAncho = 80;
    const imagenAlto = 80;

    // Añadir la imagen al PDF
    doc.image(rutaImagen, imagenX, imagenY, { width: imagenAncho, height: imagenAlto });


    
    doc.moveDown(1.5);


    // Definir coordenadas y dimensiones del rectángulo del título
    const datosRectX = 50;
    const datosRectY = 140;
    const datosRectWidth = 500;
    const datosRectHeight = 600;
    const datosRectBorderWidth = 2;

    // Dibujar el rectángulo del título con borde negro y fondo gris
    doc.rect(datosRectX, datosRectY, datosRectWidth, datosRectHeight)
        .lineWidth(datosRectBorderWidth)
        .fillColor('#000000') // Color de fondo gris
        .strokeColor('#787878') // Color de borde negro
        .stroke()
        .fill();

    // Datos del formulario
    const campos = [
      { titulo: 'Nombre', valor: req.body.nombre },
      { titulo: 'Apellido', valor: req.body.apellido },
      { titulo: 'Sexo', valor: req.body.sexo },
      { titulo: 'Fecha de Nacimiento', valor: req.body.fechaNacimiento },
      { titulo: 'Edad', valor: req.body.edad },
      { titulo: 'Peso (kg)', valor: req.body.peso },
      { titulo: 'Altura (cm)', valor: req.body.altura },
      { titulo: 'Alergias', valor: req.body.alergias },
      { titulo: 'Notas', valor: req.body.notas },
      { titulo: 'Fecha Actual', valor: obtenerFechaActual() }
    ];

    doc.fontSize(12);
    doc.strokeColor('#787878');
    let actual = 0;
    // Agregar campos al documento con separación
    campos.forEach((campo, index) => {
      const yPos = 100 + index * 20;
      doc.text(`${campo.titulo}: `, { align: 'left', continued: true })
          .text(campo.valor, { align: 'left', continued: false, indent: 100 });
      doc.moveDown(0.3).lineWidth(1).moveTo(50, doc.y).lineTo(550, doc.y).stroke();
      doc.moveDown();
      actual = yPos;
    });

    const enfermedades = Object.keys(req.body).filter(key => key !== "_csrf").slice(7);
    const enfermedadesMarcadas = enfermedades.slice(0,enfermedades.length-2);
    console.log(enfermedadesMarcadas);
    // Posición inicial
    let xPos = 80;
    let yPos = actual+ 210;
    const spaceBetweenElements = 5;
    const maxPosX = 500; // Posición X máxima antes de un salto de línea

    doc.text(`Enfermedades: `, { align: 'left', continued: true });

    let firstElement = true; // Variable para controlar el primer elemento

    enfermedadesMarcadas.forEach((enfermedad, index) => {
      // Calculamos el ancho de la enfermedad

      console.log(enfermedad, " ")
      const enfermedadWidth = doc.widthOfString(enfermedad);
  
      // Verificamos si la enfermedad excede el espacio disponible en la línea actual
      if (xPos + enfermedadWidth > maxPosX) {
          xPos = 80; // Reiniciamos la posición X para iniciar una nueva línea
          yPos += 20; // Aumentamos la posición Y para el salto de línea
      }
  
      // Agregamos la enfermedad al PDF
      console.log("Escribo " + enfermedad + "en x, y: " + xPos + " " + yPos);
      if(index != 0)doc.text(enfermedad + ",", xPos, yPos);
      else{
        doc.text("   ", xPos, yPos);
        xPos -= (enfermedadWidth + spaceBetweenElements);
      }
      // Actualizamos la posición X para el próximo elemento
      xPos += enfermedadWidth + spaceBetweenElements; // Agregamos espacio entre enfermedades
  });
  
  
  
    
    //doc.rect(10, 50, 600, 800).fill('lightgrey');

    // Creamos un flujo de escritura para guardar el PDF en un archivo local
    const filePath = "../MedAlerta/public/historiales/HM"+req.session.currentUser.dni + "-" + req.body.dni + ".pdf";
    const writeStream = fs.createWriteStream(filePath);

    // Manejamos eventos de error y finalización del flujo de escritura
    writeStream.on('finish', () => {
      res.render('gestionUsuarios',{nombre : req.session.currentUser.nombre});
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

router.get('/obtenerURLPDF', (req, res) => {
  const pdfURL = "/historiales/HM" + req.session.currentUser.dni + "-" + req.query.dni + ".pdf";

  // Responde con la URL del PDF
  res.json({ downloadURL: pdfURL });
});

module.exports = router;
