"use strict";

class DAODoctor{
    constructor(pool){
        this.pool = pool;
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

}

module.exports = DAODoctor;