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

}

module.exports = DAOPaciente;