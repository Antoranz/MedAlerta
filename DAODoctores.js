"use strict";

const fs = require('fs');

class DAODoctor{
    constructor(pool){
        this.pool = pool;
    }

    aniadirDoctor(dni,email,hashedPassword,nombre,apellidos,fecha_nacimiento,domicilio,codigo_postal,numero_telefono){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    reject(err);
                }else{
                    var queryAnyadirDoctor ="INSERT INTO doctores (dni,email,password,nombre,apellidos,fecha_nacimiento, domicilio, codigo_postal, numero_telefono) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    connection.query(queryAnyadirDoctor,[dni,email,hashedPassword,nombre,apellidos,fecha_nacimiento,domicilio,codigo_postal,numero_telefono], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }
    updateDoctor(dni, email, nombre, apellidos, fecha_nacimiento, domicilio, codigo_postal, numero_telefono) {
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if (err) {
                    reject(err);
                } else {
                    var queryActualizarDoctor = "UPDATE doctores SET email=?, nombre=?, apellidos=?, fecha_nacimiento=?, domicilio=?, codigo_postal=?, numero_telefono=? WHERE dni=?";
                    connection.query(queryActualizarDoctor, [email, nombre, apellidos, fecha_nacimiento, domicilio, codigo_postal, numero_telefono, dni], (err, res) => {
                        connection.release();
                        if (err) {
                            reject(err);
                        } else {
                            resolve(res);
                        }
                    });
                }
            });
        });
    }
    
    aniadirUsuario(DNIDoctor,DNIPaciente){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryAnyadirUsuario ="INSERT INTO asignaciones (DNIDoctor, DNIPaciente) VALUES (?, ?)"
                    connection.query(queryAnyadirUsuario,[DNIDoctor,DNIPaciente], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }
    
    eliminarAsociacion(DNIDoctor,DNIPaciente){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryeliminarAsociacion ="DELETE FROM asignaciones WHERE DNIDoctor = ? AND DNIPaciente = ?"
                    connection.query(queryeliminarAsociacion,[DNIDoctor,DNIPaciente], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }
    eliminarCita(id){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryeliminarCita ="DELETE FROM citas WHERE id = ?"
                    connection.query(queryeliminarCita,[id], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    eliminarNotificacion(id){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryeliminarNotificacion ="DELETE FROM notificaciones WHERE id = ?"
                    connection.query(queryeliminarNotificacion,[id], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }
    asignarCita(id_doctor,id_paciente,fecha,duracion){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryGuardarTratamiento ="INSERT INTO citas (fecha_hora,duracion,doctor_dni,paciente_dni) VALUES (?,?,?,?)"
                    connection.query(queryGuardarTratamiento,[fecha,duracion,id_doctor,id_paciente], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }
    checkearCitasCoincidentes(DNIDoctor, fecha_hora, duracion) {
        // Obtener los componentes de fecha y hora
        const fecha = fecha_hora.split(' ')[0]; // Extraer la parte de la fecha (YYYY-MM-DD)
        const hora = fecha_hora.split(' ')[1]; // Extraer la parte de la hora (HH:MM:SS)
    
        // Formatear la fecha y hora para la consulta SQL
        const fecha_hora_formateada = fecha + ' ' + hora;
        
        console.log("fecha de las citas coincidentes que he pasado:" + fecha_hora_formateada)
    
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if (err) {
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                } else {
                    console.log("Conexión exitosa a la base de datos");
                    var query = `SELECT paciente_dni, fecha_hora
                        FROM citas
                        WHERE doctor_dni = ? 
                        AND (
                            -- Verificar que la fecha y hora de inicio de la cita esté dentro del intervalo de una cita existente
                            EXISTS (
                                SELECT *
                                FROM citas c
                                WHERE c.doctor_dni = ? 
                                AND c.fecha_hora <= ?
                                AND ADDTIME(c.fecha_hora, SEC_TO_TIME(c.duracion * 60)) > ?
                            )
                            OR
                            -- Verificar que la fecha y hora de fin de la cita esté dentro del intervalo de una cita existente
                            EXISTS (
                                SELECT *
                                FROM citas c
                                WHERE c.doctor_dni = ? 
                                AND ADDTIME(c.fecha_hora, SEC_TO_TIME(c.duracion * 60)) > ?
                                AND c.fecha_hora < ?
                            )
                            OR
                            -- Verificar que la fecha y hora de inicio de la cita y la fecha y hora de fin de la cita abarquen una cita existente
                            EXISTS (
                                SELECT *
                                FROM citas c
                                WHERE c.doctor_dni = ? 
                                AND c.fecha_hora >= ?
                                AND ADDTIME(c.fecha_hora, SEC_TO_TIME(c.duracion * 60)) <= ?
                            )
                            OR
                            -- Verificar que la fecha y hora de inicio de la cita esté después del final de la cita actual
                            EXISTS (
                                SELECT *
                                FROM citas c
                                WHERE c.doctor_dni = ? 
                                AND c.fecha_hora > ?
                                AND c.fecha_hora < ADDTIME(?, SEC_TO_TIME(? * 60))
                            )
                        )
                        ORDER BY fecha_hora;`;
    
                    connection.query(query, [DNIDoctor, DNIDoctor, fecha_hora_formateada, fecha_hora_formateada, DNIDoctor, fecha_hora_formateada, fecha_hora_formateada, DNIDoctor, fecha_hora_formateada, fecha_hora_formateada, DNIDoctor, fecha_hora_formateada, fecha_hora_formateada, fecha_hora_formateada, duracion], (err, res) => {
                        connection.release();
                        if (err) {
                            reject(err);
                        } else {
                            resolve(res);
                        }
                    });
                }
            });
        });
    }
    
    
    guardarTratamiento(id_doctor,id_paciente,diagnostico){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryGuardarTratamiento ="INSERT INTO tratamiento (id_paciente, id_doctor, diagnostico) VALUES (?, ?, ?)"
                    connection.query(queryGuardarTratamiento,[id_paciente,id_doctor,diagnostico], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    guardarAlarma(id_tratamiento,medicamento,dosis,hora_primera_toma,tomas_al_dia,fecha_inicio,fecha_fin){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryGuardarAlarma ="INSERT INTO alarma (id_tratamiento,medicamento,dosis,hora_primera_toma,tomas_al_dia,fecha_inicio,fecha_fin) VALUES (?, ?, ?, ?, ?, ?, ?)"
                    connection.query(queryGuardarAlarma,[id_tratamiento,medicamento,dosis,hora_primera_toma,tomas_al_dia,fecha_inicio,fecha_fin], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    checkCitas(DNIDoctor){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryobtenerCitas_doctor =`SELECT citas.*, pacientes.nombre AS nombre_paciente, pacientes.apellidos AS apellidos_paciente
                    FROM citas
                    JOIN pacientes ON citas.paciente_dni = pacientes.dni
                    WHERE citas.doctor_dni = ?;
                    `;
                    connection.query(queryobtenerCitas_doctor,[DNIDoctor], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }
    obtenerPacientes_doctor(DNIDoctor){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryobtenerPacientes_doctor ="SELECT p.* FROM asignaciones AS a INNER JOIN pacientes AS p ON a.DNIPaciente = p.dni WHERE a.DNIDoctor = ?"
                    connection.query(queryobtenerPacientes_doctor,[DNIDoctor], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    obtenerNotificaciones(doctor_dni){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryobtenerNotificaciones ="SELECT * FROM notificaciones WHERE doctor_dni = ?"
                    connection.query(queryobtenerNotificaciones,[doctor_dni], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    obtenerConsultas_doctor(DNIDoctor){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryobtenerConsultas_doctor ="SELECT DISTINCT id, dni_paciente, dni_doctor, titulo, ultima_fecha, notificaciones_doctor FROM consultas WHERE dni_doctor = ? ORDER BY ultima_fecha DESC"
                    connection.query(queryobtenerConsultas_doctor,[DNIDoctor], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    obtenerMensajes_consulta(id){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryobtenerMensajes_consulta ="SELECT * FROM mensajes WHERE id_consulta = ? ORDER BY fecha ASC"
                    connection.query(queryobtenerMensajes_consulta,[id], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    bajaDoctor_citas(doctor_dni){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var querybajaDoctor_citas ="DELETE FROM citas WHERE doctor_dni = ?"
                    connection.query(querybajaDoctor_citas,[doctor_dni], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    bajaDoctor_tratamientos(id_doctor){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var querybajaDoctor_tratamientos ="DELETE FROM tratamiento WHERE id_doctor = ?"
                    connection.query(querybajaDoctor_tratamientos,[id_doctor], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    bajaDoctor(dni){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var querybajaDoctor ="DELETE FROM doctores WHERE dni = ?"
                    connection.query(querybajaDoctor,[dni], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    bajaDoctor_asignaciones(DNIDoctor){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var querybajaDoctor_asignaciones ="DELETE FROM asignaciones WHERE DNIDoctor = ?"
                    connection.query(querybajaDoctor_asignaciones,[DNIDoctor], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    bajaDoctor_consultas(dni_doctor){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var querybajaDoctor_consultas ="DELETE FROM consultas WHERE dni_doctor = ?"
                    connection.query(querybajaDoctor_consultas,[dni_doctor], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    bajaDoctor_notificaciones(doctor_dni){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var querybajaDoctor_notificaciones ="DELETE FROM notificaciones WHERE doctor_dni = ?"
                    connection.query(querybajaDoctor_notificaciones,[doctor_dni], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    checkDoctor(dni,password){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryCheckDoctor ="SELECT * FROM doctores WHERE dni = ? AND password = ?"
                    connection.query(queryCheckDoctor,[dni,password], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    /*mandarMensaje(consultaId,texto,propietario,fecha){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryGuardarAlarma ="INSERT INTO mensajes (id_consulta,mensaje,propietario,fecha) VALUES (?, ?, ?, ?)"
                    connection.query(queryGuardarAlarma,[consultaId,texto,propietario,fecha], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }*/

    mandarMensaje(consultaId, texto, propietario, fecha) {
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if (err) {
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                } else {
                    console.log("Exito al conectar a la base de datos");

                    var queryInsertar = "INSERT INTO mensajes (id_consulta, mensaje, propietario, fecha) VALUES (?, ?, ?, ?)";
                    connection.query(queryInsertar, [consultaId, texto, propietario, fecha], (err, resInsert) => {
                        if (err) {
                            connection.release();
                            reject(err);
                        } else {
                            console.log("Mensaje insertado con éxito");

                            var queryActualizar = "UPDATE consultas SET notificaciones_paciente = notificaciones_paciente + 1 WHERE id = ?";
                            connection.query(queryActualizar, [consultaId], (err, resUpdate) => {
                                connection.release(); // Siempre libera la conexión después del último uso
                                if (err) {
                                    reject(err);
                                } else {
                                    resolve({ insert: resInsert, update: resUpdate });
                                }
                            });
                        }
                    });
                }
            });
        });
    }
    

    borrarNotificaciones(consultaId,propietario){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryGuardarAlarma ="UPDATE consultas SET notificaciones_doctor = 0 WHERE id = ?"
                    connection.query(queryGuardarAlarma,[consultaId], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    
    aniadirEnfermedades(dniDoctor){
        return new Promise((resolve, reject) => {
            fs.readFile("./public/data/enfermedades.json", "utf8", (err, data) => {
                if (err) {
                    console.error(`Error al leer el archivo JSON: ${err.message}`);
                    reject(err);
                    return;
                }
    
                const enfermedadesJson = JSON.parse(data);
                const enfermedades= enfermedadesJson.enfermedades;
                if (!Array.isArray(enfermedades)) {
                    reject(new Error("El archivo JSON no contiene un array de enfermedades"));
                    return;
                }
    
                this.pool.getConnection((err, connection) => {
                    if (err) {
                        console.error(`Error al realizar la conexión: ${err.message}`);
                        reject(err);
                        return;
                    }
    
                    console.log("Éxito al conectar a la base de datos");
    
                    const promises = enfermedades.map(enfermedad => {
                        const queryGuardarAlarma = "INSERT INTO enfermedades (doctor_dni, enfermedad) VALUES (?, ?)";
                        return new Promise((resolve, reject) => {
                            connection.query(queryGuardarAlarma, [dniDoctor, enfermedad], (err, res) => {
                                if (err) {
                                    reject(err);
                                } else {
                                    resolve(res);
                                }
                            });
                        });
                    });
    
                    Promise.all(promises)
                        .then(results => {
                            connection.release();
                            resolve(results);
                        })
                        .catch(err => {
                            connection.release();
                            reject(err);
                        });
                });
            });
        });
    }

    getEnfermedades(dni){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryCheckDoctor ="SELECT enfermedad FROM enfermedades WHERE doctor_dni = ?"
                    connection.query(queryCheckDoctor,[dni], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

    
    aniadirEnfermedad(doctor_dni, enfermedad){
        console.log("la enfermedad X es: " + enfermedad)
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryGuardarTratamiento ="INSERT INTO enfermedades (doctor_dni, enfermedad) VALUES (?,?)"
                    connection.query(queryGuardarTratamiento,[doctor_dni, enfermedad], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
                }
            });
        }); 
    }

}

module.exports = DAODoctor;