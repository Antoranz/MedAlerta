var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  if(req.session.currentUser == undefined || req.session.currentUser == null || req.session.currentUser == ""){
    res.render('main', { email:"" });
  }else{
    res.render('main', { email: req.session.currentUser.email });
  }
});
router.get('/iniciar-sesion', function(req, res, next) {
  res.render('index', { email:"" });
});
router.get('/signout', function(req, res, next) {
  req.session.destroy();
  res.redirect("/");
});
module.exports = router;
