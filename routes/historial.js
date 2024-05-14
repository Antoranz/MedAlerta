var express = require('express');
var router = express.Router();
const PDFDocument = require('pdfkit');
const fs = require('fs');
const path = require('path');
const DAOPaciente = require("../DAOPacientes")

const mysql = require('mysql');
const pool = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'medalerta'
});

const dao = new DAOPaciente(pool);

router.get('/CrearHistorial/', async function(req, res, next) {
    if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
      res.render('index', { nombre:"" });
    }else{
      console.log(req.query.dni);

      var paciente = await dao.obtenerPaciente(req.query.dni);

      res.render('historialMedico', { title: 'Historial Médico' ,userData: paciente[0],nombre:req.session.currentUser.nombre, dni:req.session.currentUser.dni});
    }
});
    
router.post('/guardarHistorial', function(req, res){

  const data = req.body;

  aniadirJson(req.session.currentUser.dni, data);

  try{
    crearPDF(req.session.currentUser.dni, data);
    res.redirect('/doctor/funciones-paciente/'+data.dni);
  }
  catch(error){
    console.log(error.message)
    res.redirect('/doctor/funciones-paciente/'+data.dni);
  }

 
});

router.post('/aniadirDetalles', (req, res) => {
  const dniPaciente = req.body.dni
  const nombrePaciente = req.body.nombrePaciente
  const apellidosPaciente = req.body.apellidosPaciente
  const nuevoDetalle = req.body.detalles; 

  const filePath =  path.join(path.dirname(__dirname), 'private', 'historiales', 'json', `HM${req.session.currentUser.dni}-${dniPaciente}.json`);

  fs.readFile(filePath, 'utf8', (err, data) => {
    if (err) {
      res.render("funcionesUsuario",{ nombre:req.session.currentUser.nombre,nombrePaciente:nombrePaciente,apellidosPaciente:apellidosPaciente,dni:dniPaciente, correct:"", error:"Error al leer el historial"});
    }
    else{
      if(typeof data != 'undefined'){
        let historial
        historial = JSON.parse(data);
  
  
        if (typeof historial.detalles === 'undefined') {
          historial.detalles = [];
        } else if (!Array.isArray(historial.detalles)) {
          historial.detalles = [historial.detalles];
        }
  
        historial.detalles.push(nuevoDetalle);
  
        fs.writeFile(filePath, JSON.stringify(historial, null, 2), (err) => {
          if (err) {
            res.render("funcionesUsuario",{ nombre:req.session.currentUser.nombre,nombrePaciente:nombrePaciente,apellidosPaciente:apellidosPaciente,dni:dniPaciente, correct:"", error:"Error al escribir el historial"});
          }
          else{
            fs.readFile(filePath, 'utf8', (err, data) => {
              if (err) {
                res.render("funcionesUsuario",{ nombre:req.session.currentUser.nombre,nombrePaciente:nombrePaciente,apellidosPaciente:apellidosPaciente,dni:dniPaciente, correct:"", error:"Error al escribir el historial"});
              }else{
                let historial = JSON.parse(data);
                try{
                  crearPDF(req.session.currentUser.dni, historial);
                }
                catch(error){
                  res.render("funcionesUsuario",{ nombre:req.session.currentUser.nombre,nombrePaciente:nombrePaciente,apellidosPaciente:apellidosPaciente,dni:dniPaciente, correct:"", error:"Ha surgido un error al crear el historial"});
                }
                res.render("funcionesUsuario",{ nombre:req.session.currentUser.nombre,nombrePaciente:nombrePaciente,apellidosPaciente:apellidosPaciente,dni:dniPaciente, correct:"Detalle añadido correctamente", error:""});
              }
            });
          }
          
        });
  
      }
      else{
        res.render("funcionesUsuario",{ nombre:req.session.currentUser.nombre,nombrePaciente:nombrePaciente,apellidosPaciente:apellidosPaciente,dni:dniPaciente, correct:"", error:"Historial no existe"});
      }
    }

  });
});

router.get('/obtenerPDF', async (req, res) => {

  const dniPaciente = req.query.dniPaciente;

  const pdfURL = "/historiales/HM" + req.session.currentUser.dni + "-" + dniPaciente + ".pdf";
  const filePath = path.join(path.dirname(__dirname), 'private', pdfURL);
  let filePathExiste = await fs.existsSync(filePath);
  if ( !filePathExiste) {
    res.status(404).send("No existe el historial");
  }
  else{
    res.sendFile(filePath);
  }
});

router.delete('/eliminar', (req, res) => {

    const dniPaciente = req.body.dniPaciente;
    const nombrePaciente = req.body.nombrePaciente;
    const apellidosPaciente = req.body.apellidosPaciente;

    const pdfURL = path.join(path.dirname(__dirname), 'private', 'historiales', `HM${req.session.currentUser.dni}-${dniPaciente}.pdf`);
    const filePath =  path.join(path.dirname(__dirname), 'private', 'historiales', 'json', `HM${req.session.currentUser.dni}-${dniPaciente}.json`);

    if (!dniPaciente) {
      res.render("funcionesUsuario",{ nombre:req.session.currentUser.nombre,nombrePaciente:nombrePaciente,apellidosPaciente:apellidosPaciente,dni:dniPaciente, correct:"", error:"DNI del paciente es necesario"});
    } else {
        fs.unlink(pdfURL, (err) => {
            if (err) {
              if (err.code === 'ENOENT') {
                res.status(404).send("Archivo no encontrado");
              } else {
                res.status(400).send("Archivo no encontrado");
              }
            } else {
              fs.unlink(filePath, (err) => {
                if (err) {
                  if (err.code === 'ENOENT') {
                    res.status(404).send("Archivo no encontrado");
                  } else {
                    res.status(404).send("Error al eliminar el archivo");
                  }
                } else {
                  res.render("funcionesUsuario",{ nombre:req.session.currentUser.nombre,nombrePaciente:nombrePaciente,apellidosPaciente:apellidosPaciente,dni:dniPaciente, correct:"Archivo eliminado con éxito", error:""});
                }
              });
            }
          });
    }
  });

  function aniadirJson(dniDoctor, data){

    const jsonPath = path.join(path.dirname(__dirname), 'private', 'historiales', 'json', `HM${dniDoctor}-${data.dni}.json`);
  
    fs.writeFile(jsonPath, JSON.stringify(data, null, 2), (err) => {
      if (err) {
        console.error('Error al escribir en el archivo JSON:', err);
      }
    });
  }

  function crearPDF(dniDoctor, data){
    const checkboxesSeleccionados = Object.keys(data).filter(key => {
        return data[key] === 'on';
    });
    const doc = new PDFDocument();
    
    const tituloRectX = 50;
    const tituloRectY = 50;
    const tituloRectWidth = 500;
    const tituloRectHeight = 80;
    const tituloRectBorderWidth = 2;

    doc.rect(tituloRectX, tituloRectY, tituloRectWidth, tituloRectHeight)
        .lineWidth(tituloRectBorderWidth)
        .fillColor('#CCCCCC')
        .strokeColor('#000000')
        .stroke()
        .fill();


    doc.font('Helvetica-Bold').fontSize(24)
        .fillColor('#000000') 
        .text('Historial Médico', { align: 'center', lineGap: 10 });


    doc.font('Helvetica').fontSize(16)
        .text(obtenerFechaActual(), { align: 'center' });


    const rutaImagen = './public/images/logoMedAlerta.png';


    const imagenX = 70;
    const imagenY = 50;
    const imagenAncho = 80;
    const imagenAlto = 80;

    doc.image(rutaImagen, imagenX, imagenY, { width: imagenAncho, height: imagenAlto });

    doc.moveDown(1.5);

    const datosRectX = 50;
    const datosRectY = 140;
    const datosRectWidth = 500;
    const datosRectHeight = 600;
    const datosRectBorderWidth = 2;


    doc.rect(datosRectX, datosRectY, datosRectWidth, datosRectHeight)
        .lineWidth(datosRectBorderWidth)
        .fillColor('#000000') 
        .strokeColor('#787878')
        .stroke()
        .fill();


    const campos = [
    { titulo: 'Nombre', valor: data.nombre },
    { titulo: 'Apellido', valor: data.apellido },
    { titulo: 'Sexo', valor: data.sexo },
    { titulo: 'Fecha de Nacimiento', valor: data.fechaNacimiento },
    { titulo: 'Peso (kg)', valor: data.peso },
    { titulo: 'Altura (cm)', valor: data.altura },
    { titulo: 'Alergias', valor: data.alergias },
    { titulo: 'Notas', valor: data.notas },
    { titulo: 'Fecha Actual', valor: obtenerFechaActual() }
    ];

    doc.fontSize(12);
    doc.strokeColor('#787878');
    let actual = 0;

    campos.forEach((campo, index) => {
    const yPos = 100 + index * 20;
    doc.text(`${campo.titulo}: `, { align: 'left', continued: true })
        .text(campo.valor, { align: 'left', continued: false, indent: 100 });
    doc.moveDown(0.3).lineWidth(1).moveTo(50, doc.y).lineTo(550, doc.y).stroke();
    doc.moveDown();
    actual = yPos;
    });

    const enfermedades = Object.keys(data).filter(key => key !== "_csrf").slice(6);
    const enfermedadesMarcadas = enfermedades.slice(0,enfermedades.length-3);

    let xPos = 80;
    let yPos = actual+ 210;
    const spaceBetweenElements = 5;
    const maxPosX = 500;

    doc.text(`Enfermedades: `, { align: 'left', continued: true });

    let firstElement = true;

    enfermedadesMarcadas.forEach((enfermedad, index) => {

    const enfermedadWidth = doc.widthOfString(enfermedad);

    if (xPos + enfermedadWidth > maxPosX) {
        xPos = 80;
        yPos += 20; 
    }

    if(index != 0)doc.text(enfermedad + ",", xPos, yPos);
    else{
        doc.text("   ", xPos, yPos);
        xPos -= (enfermedadWidth + spaceBetweenElements);
    }

    xPos += enfermedadWidth + spaceBetweenElements; 
  });

    doc.addPage(); 

    doc.font('Helvetica-Bold')
    doc.fontSize(18);
    doc.text("Detalles", { align: 'center', lineGap: 10 });

    doc.font('Helvetica').fontSize(12);

    const detalles = data.detalles;

    if (Array.isArray(detalles)) {
      detalles.forEach((detalle) => {
        doc.fillColor('grey')
        doc.text("-----" + obtenerFechaActual() + "-----")
        doc.fillColor('black')
        doc.text(detalle, { align: 'left', lineGap: 10 });
      });
    } else {
      doc.fillColor('grey')
      doc.text("-----" + obtenerFechaActual() + "-----")
      doc.fillColor('black')
      doc.text(detalles, { align: 'left', lineGap: 10 });
    }

    const filePath = path.join(path.dirname(__dirname), 'private', 'historiales', `HM${dniDoctor}-${data.dni}.pdf`);
    const writeStream = fs.createWriteStream(filePath);

    writeStream.on('finish', () => {
    
    });

    writeStream.on('error', (err) => {
      throw(new Error('Error al guardar el PDF'))
    });

    doc.pipe(writeStream); 

    doc.end();
  }

  function obtenerFechaActual() {
    const fecha = new Date();
    const dia = fecha.getDate();
    const mes = fecha.getMonth() + 1;
    const anio = fecha.getFullYear();
    return `${dia}/${mes}/${anio}`;
    }

module.exports = router;