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

    expect(res.render).toHaveBeenCalledWith("register",{error:"Las contraseñas no coinciden", usuario: usuario,email:""});

  });

  test('Debe dar el error correcto al usar DNI sin letra', () => {
    const req = {
        body: {
            dni: '12356789',
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
    usuario.dni = "";

    const res = {
    render: jest.fn()
    };

    registrarUsuario(req, res);

    expect(res.render).toHaveBeenCalledWith("register",{error:"El DNI no es válido", usuario: usuario,email:""});

  });

  test('Debe dar el error correcto al usar DNI con letra mal posicionada', () => {
    const req = {
        body: {
            dni: '1235A6789',
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
    usuario.dni = "";

    const res = {
    render: jest.fn()
    };

    registrarUsuario(req, res);

    expect(res.render).toHaveBeenCalledWith("register",{error:"El DNI no es válido", usuario: usuario,email:""});

  });

  test('Debe dar el error correcto al usar una fecha actual/futura', () => {
    let fecha = new Date();
    const req = {
        body: {
            dni: '12356789A',
            email: 'test@example.com',
            password: 'password123',
            repeatPassword: 'password123',
            nombre: 'John',
            apellidos: 'Doe',
            fecha_nacimiento: fecha,
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
    usuario.fecha_nacimiento = "";

    const res = {
      render: jest.fn()
      };

    registrarUsuario(req, res);

    expect(res.render).toHaveBeenCalledWith("register",{error:"La fecha de nacimiento debe ser anterior a la fecha actual", usuario: usuario,email:""});

  });

  test('Debe dar el error correcto cuando numero de digitos de telf son mayor que 9', () => {
    const req = {
        body: {
            dni: '12356789A',
            email: 'test@example.com',
            password: 'password123',
            repeatPassword: 'password123',
            nombre: 'John',
            apellidos: 'Doe',
            fecha_nacimiento: '1990-01-01',
            domicilio: '123 Main St',
            codigo_postal: '12345',
            numero_telefono: '1234567891'
        },
        session: {
            cookie:{ path: '/', _expires: null, originalMaxAge: null, httpOnly: true },
            currentUser: {}
        }
    };
    
    const {dni,email,password,repeatPassword,nombre,apellidos,fecha_nacimiento,domicilio,codigo_postal,numero_telefono} = req.body;
    var usuario = {dni: dni,email: email,password: password,repeatPassword: repeatPassword,nombre: nombre,apellidos: apellidos,fecha_nacimiento: fecha_nacimiento,domicilio: domicilio,codigo_postal: codigo_postal,numero_telefono: numero_telefono};
    usuario.numero_telefono = "";

    const res = {
      render: jest.fn()
      };

    registrarUsuario(req, res);

    expect(res.render).toHaveBeenCalledWith("register",{error:"El número de teléfono no es válido", usuario: usuario,email:""});

  });

  test('Debe dar el error correcto cuando numero de digitos de telf son menor que 9', () => {
    const req = {
        body: {
            dni: '12356789A',
            email: 'test@example.com',
            password: 'password123',
            repeatPassword: 'password123',
            nombre: 'John',
            apellidos: 'Doe',
            fecha_nacimiento: '1990-01-01',
            domicilio: '123 Main St',
            codigo_postal: '12345',
            numero_telefono: '12345678'
        },
        session: {
            cookie:{ path: '/', _expires: null, originalMaxAge: null, httpOnly: true },
            currentUser: {}
        }
    };
    
    const {dni,email,password,repeatPassword,nombre,apellidos,fecha_nacimiento,domicilio,codigo_postal,numero_telefono} = req.body;
    var usuario = {dni: dni,email: email,password: password,repeatPassword: repeatPassword,nombre: nombre,apellidos: apellidos,fecha_nacimiento: fecha_nacimiento,domicilio: domicilio,codigo_postal: codigo_postal,numero_telefono: numero_telefono};
    usuario.numero_telefono = "";

    const res = {
      render: jest.fn()
      };

    registrarUsuario(req, res);

    expect(res.render).toHaveBeenCalledWith("register",{error:"El número de teléfono no es válido", usuario: usuario,email:""});

  });

});
