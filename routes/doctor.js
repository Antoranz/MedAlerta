
var express = require('express');
var router = express.Router();

const PDFDocument = require('pdfkit');
const fs = require('fs');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
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
