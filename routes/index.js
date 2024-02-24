var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('main', { nombre:"" });
  }else{
    res.render('main', { nombre: req.session.currentUser.nombre });
  }
});
router.get('/iniciar-sesion', function(req, res, next) {
  res.render('index', { nombre:"" });
});
router.get('/signout', function(req, res, next) {
  req.session.destroy();
  res.redirect("/");
});
module.exports = router;
