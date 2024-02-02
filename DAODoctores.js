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