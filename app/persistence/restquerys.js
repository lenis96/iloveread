var exports = module.exports = {};
var connection=require('./connection');
exports.getUserInformation=function(f,user,password){
    connection.getConnection(function(con){
        con.query("call getUserDetail(?,?) ",[user,password],function(err,rows,fields){
            if(err) throw err;
            f(rows);
        });
    });
}
exports.getCategories=function(f){
    connection.getConnection(function(con) {
        con.query("call getCategories",[],function(err,rows,fields){
            if(err) throw err;
            f(rows);
        });
    });
}
exports.getInicio=function(f){
    connection.getConnection(function(con) {
        con.query('CALL getHome();',[],function(err,rows,fields){
     
            if(err) throw err;
            f(rows[0],rows[1]);
            /*
            con.query("call getMasVendidos",[],function(err,rows2,fields){
                if(err) throw err;
                f(rows,rows2);
                con.end();
            });
            */
        });
        //con.end();
    });
}
exports.getCategoria=function(idCat,f){
    connection.getConnection(function(con) {
       con.query("call getLibros(?,10)",[idCat],function(err,rows,fields){
           if(err) throw err;
           f(rows[0]);
       }) ;
    });
}
exports.comprarLibro=function (user,libroId,f){
    console.log({user,libroId});
    connection.getConnection(function(con) {
        con.query("call comprarLibro(?,?);",[libroId,user],function(err,rows,fields){
            if(err) throw err;
            f(rows);

        });
    });
}
exports.comprarLibros=function(user,f){
    connection.getConnection(function(con) {
       con.query("call comprarLibros(?)",[user],function(err,rows,fields){
         if(err) throw err;
         f(rows);
       });
    });
}
exports.agregarCarroCompras=function(user,libroId,f){
    connection.getConnection(function(con) {
       con.query("call agregarCarroCompras(?,?)",[libroId,user],function(err,rows,fields){
         if(err) throw err;
         f(rows);
       });
    });
}
exports.eliminarLibroCarrito=function(user,isbn,f){
   connection.getConnection(function(con){
       con.query("call eliminarLibroCarrito(?,?)",[user,isbn],function(err,rows,fields){
           if(err) throw err;
           f(rows);
       });
   });
}
exports.obtenerCarritoCompras=function(user,f){
    connection.getConnection(function(con) {
       con.query("call obtenerCarritoCompras(?)",[user],function(err,rows,fields){
           if(err) throw err;
           f(rows);
       }) ;
    });
}
exports.actulizarUsuario=function(user,password,name,lastname,idSex,idCity,birthDate,address,f){
    connection.getConnection(function(con) {
       con.query("call actulizarUsuario(?,?,?,?,?,?,?,?)",[user,password,name,lastname,idSex,idCity,birthDate,address],function(err,rows,fields){
           if(err) throw err;
           f(rows);
       }) ;
    });
}
exports.crearUsuario=function(user,password,name,lastname,idSex,idCity,birthDate,address,f){
    connection.getConnection(function(con) {
       con.query("call crearUsuario(?,?,?,?,?,?,?,?)",[user,password,name,lastname,idSex,idCity,birthDate,address],function(err,rows,fields){
          if(err) throw err;
          f(rows);
       }); 
    });
}
exports.obtenerLibro=function(libroId,user,f){
    connection.getConnection(function(con) {
       con.query("call obtenerLibro(?,?)",[libroId,user],function(err,rows,fields){
           if(err) throw err;
           f(rows[0]);
       }) ;
    });
}
exports.buscarLibro=function(texto,f){
    connection.getConnection(function(con) {
        con.query("call buscar (?)",[texto],function(err,rows,fields){
           if(err) throw err;
           f(rows[0]);
        });
    });
}
exports.updateE=function(user,email,f){
    connection.getConnection(function(con) {
        con.query("call actualizarE(?,?)",[user,email],function(err,rows,fields){
            if(err) throw err;
            f(rows[0]);
        }); 
    });
       
}