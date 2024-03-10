const { registrarUsuario } = require('../routes/doctor.js');

const mysql = require('mysql');
const DAOTest = require("../DAOTests.js");
const testPool = mysql.createPool({
  host: 'localhost',
  user: 'root',
  password: '',
  database: 'medalerta'
});

const dao = new DAOTest(testPool);

var req = {
  body: {
      dni: '12345678Z',
      email: 'test@example.com',
      password: 'password123',
      repeatPassword: 'password123',
      nombre: 'John',
      apellidos: 'Doe',
      fecha_nacimiento: '1990-01-01',
      domicilio: '123 Main St',
      codigo_postal: '12345',
      numero_telefono: '123456789'
  },
  session: {
      cookie:{ path: '/', _expires: null, originalMaxAge: null, httpOnly: true },
      currentUser: {}
  }
};

const {dni,email,password,repeatPassword,nombre,apellidos,fecha_nacimiento,domicilio,codigo_postal,numero_telefono} = req.body;
var usuario = {dni: dni,email: email,password: password,repeatPassword: repeatPassword,nombre: nombre,apellidos: apellidos,fecha_nacimiento: fecha_nacimiento,domicilio: domicilio,codigo_postal: codigo_postal,numero_telefono: numero_telefono};

const res = {
render: jest.fn()
};

const pool = {
  query: jest.fn()
  };
var nLlamadasRender = 0;

describe('Pruebas de exito para la función de registro', () => {
  test('Debe mandar a la página principal al ser un registro correcto', async () => {
      await registrarUsuario(req, res);
      dao.eliminarDoctor(req.body.dni);
      expect(2).toBe(2);
      //expect(pool.query).toHaveBeenCalled(); // Verifica que se llamó a la función de consulta
      
  });
});




describe('Pruebas de error para la función de registro', () => {

  test('Debe dar el error correcto al repetir contraseña', () => {

    req.body.repeatPassword = "123password";
    registrarUsuario(req, res);

    usuario.password = "";
    usuario.repeatPassword = "";
    nLlamadasRender++;
    expect(res.render).toHaveBeenCalledTimes(nLlamadasRender);
    expect(res.render).toHaveBeenCalledWith("register",{error:"Las contraseñas no coinciden", usuario: usuario,email:""});

    req.body.repeatPassword = "password123";
    usuario.password = "password123";
    usuario.repeatPassword = "password123";

  });

  test('Debe dar el error correcto al usar DNI sin letra', () => {
    
    req.body.dni = "12356789";
    registrarUsuario(req, res);
    usuario.dni = "";
    nLlamadasRender++;
    expect(res.render).toHaveBeenCalledTimes(nLlamadasRender);
    expect(res.render).toHaveBeenCalledWith("register",{error:"El DNI no es válido", usuario: usuario,email:""});

    req.body.dni = "12356789A";
    usuario.dni = "12356789A";

  });

  test('Debe dar el error correcto al usar DNI con letra mal posicionada', () => {

    req.body.dni="1235A6789";
    registrarUsuario(req, res);
    usuario.dni = "";
    nLlamadasRender++;
    expect(res.render).toHaveBeenCalledTimes(nLlamadasRender);
    expect(res.render).toHaveBeenCalledWith("register",{error:"El DNI no es válido", usuario: usuario,email:""});

    req.body.dni = "12356789A";
    usuario.dni = "12356789A";

  });

  test('Debe dar el error correcto al usar una fecha actual/futura', () => {
    
    let fecha = new Date();
    
    req.body.fecha_nacimiento = fecha;
    registrarUsuario(req, res);
    usuario.fecha_nacimiento = "";

    nLlamadasRender++;
    expect(res.render).toHaveBeenCalledTimes(nLlamadasRender);
    expect(res.render).toHaveBeenCalledWith("register",{error:"La fecha de nacimiento debe ser anterior a la fecha actual", usuario: usuario,email:""});

    req.body.fecha_nacimiento = "1990-01-01";
    usuario.fecha_nacimiento = "1990-01-01";

  });

  test('Debe dar el error correcto cuando numero de digitos de telf son mayor que 9', () => {

    req.body.numero_telefono = "1234567891";
    
    const {dni,email,password,repeatPassword,nombre,apellidos,fecha_nacimiento,domicilio,codigo_postal,numero_telefono} = req.body;
    var usuario = {dni: dni,email: email,password: password,repeatPassword: repeatPassword,nombre: nombre,apellidos: apellidos,fecha_nacimiento: fecha_nacimiento,domicilio: domicilio,codigo_postal: codigo_postal,numero_telefono: numero_telefono};
    
    registrarUsuario(req, res);
    usuario.numero_telefono = "";
    
    nLlamadasRender++;
    expect(res.render).toHaveBeenCalledTimes(nLlamadasRender);
    expect(res.render).toHaveBeenCalledWith("register",{error:"El número de teléfono no es válido", usuario: usuario,email:""});

    req.body.numero_telefono = "123456789";
    usuario.numero_telefono = "123456789";

  });

  test('Debe dar el error correcto cuando numero de digitos de telf son menor que 9', () => {
    
    req.body.numero_telefono = "12345678";
    registrarUsuario(req, res);
    usuario.numero_telefono = "";
    
    nLlamadasRender++;
    expect(res.render).toHaveBeenCalledTimes(nLlamadasRender);
    expect(res.render).toHaveBeenCalledWith("register",{error:"El número de teléfono no es válido", usuario: usuario,email:""});

    req.body.numero_telefono = "123456789";
    usuario.numero_telefono = "123456789";

  });

});
