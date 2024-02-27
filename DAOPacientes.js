"use strict";

class DAOPaciente{
    constructor(pool){
        this.pool = pool;
    }

    registrarPaciente(Nombre,Apellidos,FechaDeNacimiento,Direccion,CodigoPostal,telefono,email,DNI,password){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryRegistrarPaciente ="INSERT INTO pacientes (Nombre,Apellidos,FechaDeNacimiento,Direccion,CodigoPostal,telefono,email,DNI,password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    connection.query(queryRegistrarPaciente,[Nombre,Apellidos,FechaDeNacimiento,Direccion,CodigoPostal,telefono,email,DNI,password], (err, res) => {
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

    checkPaciente(email,password){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryCheckPaciente ="SELECT * FROM pacientes WHERE email = ? AND password = ?"
                    connection.query(queryCheckPaciente,[email,password], (err, res) => {
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

    obtenerPaciente(DNI){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryObtenerPaciente ="SELECT * FROM pacientes WHERE DNI = ?"
                    connection.query(queryObtenerPaciente,[DNI], (err, res) => {
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

    obtenerAlarmas(dni){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryObtenerAlarmas ="SELECT a.* FROM tratamiento t JOIN alarma a ON t.id_tratamiento = a.id_tratamiento JOIN pacientes p ON t.id_paciente = p.dni WHERE p.dni = ? "
                    connection.query(queryObtenerAlarmas,[dni], (err, res) => {
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

    obtenerPaciente_email(email){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryObtenerPaciente_email ="SELECT * FROM pacientes WHERE email = ?"
                    connection.query(queryObtenerPaciente_email,[email], (err, res) => {
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

    editarPaciente(Nombre,Apellidos,FechaDeNacimiento,Direccion,CodigoPostal,telefono,DNI){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryObtenerPaciente ="UPDATE pacientes SET Nombre = ?, Apellidos = ?,FechaDeNacimiento = ?,Direccion = ?, CodigoPostal = ?, telefono = ? WHERE DNI = ?"
                    connection.query(queryObtenerPaciente,[Nombre,Apellidos,FechaDeNacimiento,Direccion,CodigoPostal,telefono,DNI], (err, res) => {
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

    editarPassword(password,email){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryEditarPassword ="UPDATE pacientes SET password = ? WHERE email = ?"
                    connection.query(queryEditarPassword,[password,email], (err, res) => {
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

    bajaPaciente_alarmas(id_paciente){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var querybajaPaciente_alarmas ="DELETE a FROM alarma a INNER JOIN tratamiento t ON a.id_tratamiento = t.id_tratamiento WHERE t.id_paciente = ?"
                    connection.query(querybajaPaciente_alarmas,[id_paciente], (err, res) => {
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


    bajaPaciente_tratamientos(id_paciente){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var querybajaPaciente_tratamiento ="DELETE FROM tratamiento WHERE id_paciente = ?"
                    connection.query(querybajaPaciente_tratamiento,[id_paciente], (err, res) => {
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
    
    bajaPaciente(DNI){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var querybajaPaciente ="DELETE FROM pacientes WHERE DNI = ?"
                    connection.query(querybajaPaciente,[DNI], (err, res) => {
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

    obtenerDoctorDelPaciente(DNIPaciente){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var queryobtenerDoctorDelPaciente ="SELECT * FROM asignaciones WHERE DNIPaciente = ?"
                    connection.query(queryobtenerDoctorDelPaciente,[DNIPaciente], (err, res) => {
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

    crearNotificacionCita(tipo,fecha_hora,doctor_dni,paciente_dni){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var querycrearNotificacionCita ="INSERT INTO notificaciones (tipo,fecha_hora,doctor_dni,paciente_dni) VALUES (?, ?, ?, ?)"
                    connection.query(querycrearNotificacionCita,[tipo,fecha_hora,doctor_dni,paciente_dni], (err, res) => {
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

module.exports = DAOPaciente;