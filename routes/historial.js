var express = require('express');
var router = express.Router();
const PDFDocument = require('pdfkit');
const fs = require('fs');

router.get('/CrearHistorial/', function(req, res, next) {
    if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
      res.render('index', { nombre:"" });
    }else{
      res.render('historialMedico', { title: 'Express' ,userData: "",nombre:req.session.currentUser.nombre});
    }
});
    
router.post('/guardarHistorial', function(req, res){

    const doc = new PDFDocument();
    
    
    function obtenerFechaActual() {
    const fecha = new Date();
    const dia = fecha.getDate();
    const mes = fecha.getMonth() + 1;
    const anio = fecha.getFullYear();
    return `${dia}/${mes}/${anio}`;
    }

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

    let xPos = 80;
    let yPos = actual+ 210;
    const spaceBetweenElements = 5;
    const maxPosX = 500;

    doc.text(`Enfermedades: `, { align: 'left', continued: true });

    let firstElement = true;

    enfermedadesMarcadas.forEach((enfermedad, index) => {

    console.log(enfermedad, " ")
    const enfermedadWidth = doc.widthOfString(enfermedad);

    if (xPos + enfermedadWidth > maxPosX) {
        xPos = 80;
        yPos += 20; 
    }


    console.log("Escribo " + enfermedad + "en x, y: " + xPos + " " + yPos);
    if(index != 0)doc.text(enfermedad + ",", xPos, yPos);
    else{
        doc.text("   ", xPos, yPos);
        xPos -= (enfermedadWidth + spaceBetweenElements);
    }

    xPos += enfermedadWidth + spaceBetweenElements; 
});

    const filePath = "../MedAlerta/public/historiales/HM"+req.session.currentUser.dni + "-" + req.body.dni + ".pdf";
    const writeStream = fs.createWriteStream(filePath);

    writeStream.on('finish', () => {
    res.render('gestionUsuarios',{nombre : req.session.currentUser.nombre});
    });

    writeStream.on('error', (err) => {
        console.error('Error al guardar el PDF:', err);
        res.status(500).send('Error al guardar el PDF.');
    });

    doc.pipe(writeStream); 

    doc.end();
});

router.get('/obtenerURLPDF', (req, res) => {
    const pdfURL = "/historiales/HM" + req.session.currentUser.dni + "-" + req.query.dni + ".pdf";

    res.json({ downloadURL: pdfURL });
});

module.exports = router;