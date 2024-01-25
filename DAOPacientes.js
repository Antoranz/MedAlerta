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
    
    bajaPaciente(idPaciente){
        
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    console.error(`Error al realizar la conexión: ${err.message}`);
                    reject(err);
                }else{
                    console.log("Exito al conectar a la base de datos");
                    var querybajaPaciente ="DELETE FROM pacientes WHERE idPaciente = ?"
                    connection.query(querybajaPaciente,[idPaciente], (err, res) => {
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