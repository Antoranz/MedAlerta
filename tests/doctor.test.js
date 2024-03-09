const { registrarUsuario } = require('../routes/doctor.js');


describe('Pruebas para la función de registro', () => {
  /*test('Debe manejar el registro correctamente con datos válidos', () => {
    const req = {
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

    // Se simula también la función de respuesta
    const res = {
    render: jest.fn()
    };

    registrarUsuario(req, res);

    expect(res.render).toHaveBeenCalledWith("index",{nombre : ""});
  });*/

  test('Debe dar el error correcto al repetir contraseña', () => {
    const req = {
        body: {
            dni: '12345678Z',
            email: 'test@example.com',
            password: '123password',
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
    usuario.password = "";
    usuario.repeatPassword = "";
    const res = {
    render: jest.fn()
    };

    registrarUsuario(req, res);

    // Se espera -> res.render("register",{error:"Las contraseñas no coinciden", usuario: usuario,email:""});
    expect(res.render).toHaveBeenCalledWith("register",{error:"Las contraseñas no coinciden", usuario: usuario,email:""});

  });

});
