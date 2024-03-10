"use strict";

class DAOTest{
    constructor(pool){
        this.pool = pool;
    }

    eliminarDoctor(DNIDoctor){
        return new Promise((resolve, reject) => {
            this.pool.getConnection((err, connection) => {
                if(err){
                    reject(err);
                }else
                    var queryEliminarDoctor ="DELETE FROM doctores WHERE dni = ?";
                    connection.query(queryEliminarDoctor,[DNIDoctor], (err, res) => {
                        connection.release();
                        if(err){
                            reject(err);
                        }
                        else{
                            resolve(res);
                        }
                    });
            });
        });
    }; 
}

module.exports = DAOTest;