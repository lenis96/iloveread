var express=require('express');
var router=express.Router();
var restquerys=require('../persistence/restquerys');

router.get('/user',function(req,res){
    restquerys.getUserInformation(function(rows){
        if(rows[0].length==1){
            res.json({validate:"ok",name:rows[0][0].name,lastname:rows[0][0].lastname,email:rows[0][0].email});
        }else{
            res.json({validate:"no"});
        }
    },req.query.user,req.query.password);
});
router.get('/categorie',function(req,res){
    restquerys.getCategories(function(rows){
        
        res.json({cat:rows[0]});
    });
});
router.post('/usuario',function(req,res){
    res.status(201).send('');
});
router.get('/libro',function(req,res){
    res.json({titulo:"El origen de las especies",descripcion:"kmcdajnvsibivbsinvdsnvhbsubey",precio:34000});
});
router.get('/Libros',function(req, res) {
    restquerys.getInicio(function(rows,rows2){
        res.json({nuevos:rows,masVendidos:rows2});
    });
    //res.json({nuevos:[{titulo:"nose1",autor:"tu cucha"},{titulo:"prueba si lol",autor:"tu cucha"},{titulo:"prueba si lol",autor:"tu cucha"}],masVendidos:[{titulo:"super buy",autor:"tu cucha"}]});
});
router.get('/Libros/:idCat',function(req, res) {
    console.log(req.params.idCat);
    restquerys.getCategoria(req.params.idCat,function(rows){
        res.json({nuevos:rows});
    });
    //res.json({nuevos:[{titulo:"nose1",autor:"tu cucha"},{titulo:"prueba si lol",autor:"tu cucha"},{titulo:"prueba si lol",autor:"tu cucha"}],masVendidos:[{titulo:"super buy",autor:"tu cucha"}]});
});
router.get('/Libro/:idLibro',function(req, res) {
    restquerys.obtenerLibro(req.params.idLibro,req.query.user,function(rows){
        //console.log(rows);
       res.json(rows[0]); 
    });
});
router.post('/comprarLibro',function(req,res){
   console.log("el usuario "+req.body.usuario+" compro "+req.body.libroId);
   restquerys.comprarLibro(req.body.usuario,req.body.libroId,function(rows){
     res.status(201);
     res.send();
   });
   
});
router.post('/listaDeseos',function(req,res){
   console.log("el usuario "+req.body.usuario+" listaDeseos "+req.body.libroId);
   res.status(201);
   res.send();
});

router.delete('/listaDeseos',function(req,res){
   console.log("el usuario "+req.body.usuario+" listaDeseos "+req.body.libroId);
   res.status(201);
   res.send();
});
router.post('/carritoCompras',function(req,res){
   restquerys.agregarCarroCompras(req.body.usuario,req.body.libroId,function(rows){
     res.status(201);
     res.send();
   });
});

router.delete('/carritoCompras/:idLibro',function(req,res){
    restquerys.eliminarLibroCarrito(req.query.user,req.params.idLibro,function(rows){
        res.status(201);
   res.send();     
    });
   
});
router.post('/comprarLibros',function(req, res) {
    restquerys.comprarLibros(req.body.usuario,function(rows){
        //console.log(rows);
        var nodemailer = require('nodemailer');

        var transporter = nodemailer.createTransport({
          service: 'gmail',
          auth: {
                user: "lenisandres5@gmail.com",
                pass: ''
            }
            });
        var html="<ul>";
        for(var i=0;i<rows[1].length;i++){
            html+='<h3><a href="https://iloveread-lenis96.c9users.io/img/'+rows[1][i].pdfsrc+'">'+rows[1][i].title+"</a></h3>";
        }
        html+="</ul>"
        var mailOptions = {
          from: 'lenisandres5@gmail.com',
          to: rows[0][0].email,
          subject: 'Uste a comprado libros',
          html:html
        };

        transporter.sendMail(mailOptions, function(error, info){
        if (error) {
            console.log(error);
        }
        else {
        console.log('Email sent: ' + info.response);
        }
    });
        
        res.status(201);
        res.send();
   });
});
router.put('/user',function(req,res){
    restquerys.actulizarUsuario(req.body.user,req.body.password,req.body.name,req.body.lastName,req.body.idSex,req.body.idCity,req.body.date,req.body.address,function(rows){
       res.status(201);
        res.send(); 
    });
   
});
router.get('/carritoCompras',function(req, res) {
   restquerys.obtenerCarritoCompras(req.query.usuario,function(rows){
       var total=0;
       for(var i=0;i<rows[0].length;i++){
           total+=rows[0][i].precio;
       }
      res.json({carrito:rows[0],total:total}); 
   }); 
});
router.get('/busqueda/:busc',function(req, res) {
    console.log(req.params.busc);
   restquerys.buscarLibro(req.params.busc,function(rows){
       res.json({encontrados:rows});
   }) ;
});
router.put('/userE',function(req,res){
    console.log(req.body.email);
    restquerys.updateE(req.body.user,req.body.email,function(rows){
        res.status(201);
        res.send(); 
    });
});
module.exports=router;