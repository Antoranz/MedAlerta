const nodemailer = require("nodemailer");

const transporter = nodemailer.createTransport({
  host: "smtp.gmail.com",
  port: 465,
  secure: true,
  auth: {
    user: "medalertatfg@gmail.com",
    pass: "ibjw cugk nzup qdqr",

  },
});

async function sendVerificationEmail(correo_destinatario,contenido) {
    try {
        const info = await transporter.sendMail({
            from: '"MedAlerta" <medalertatfg@gmail.com>',
            to: correo_destinatario,
            subject: "Código de verificación",
            html: generateNotificationHTML(contenido)
        });

        console.log("Correo de notificación enviado:", info);

    } catch (error) {
        console.error("Error al enviar el correo de notificación:", error);
    }
}

function generateNotificationHTML(contenido) {
    return` 
        <p>Codigo de verificación: <strong>${contenido}</strong></p>
        <br>
        <p>Atentamente,</p>
        <p>MedAlerta</p>`
    ;
}

module.exports = {sendVerificationEmail};