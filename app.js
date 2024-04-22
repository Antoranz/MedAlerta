var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var indexRouter = require('./routes/index');
var pacientesRouter = require('./routes/pacientes');
var {router} = require('./routes/doctor');
var doctorRouter = router;



const session = require('express-session');
const mysqlSession = require("express-mysql-session");
const MySQLStore = mysqlSession(session);
const sessionStore = new MySQLStore({
  host:"localhost",
  user:"root",
  password:"",
  database:"medalerta"
});

var app = express();

const middlewareSession = session({
  saveUninitialized: false,
  secret: "foobar34",
  resave: false,
  store: sessionStore
});

app.use(middlewareSession);

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/pacientes',pacientesRouter);
app.use('/doctor', doctorRouter);


// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});



// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {}; 

  // render the error page
  res.status(err.status || 500);
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('error', { nombre:"" });
  }else{
    res.render('error', { nombre: req.session.currentUser.nombre });
  }
});

module.exports = app;