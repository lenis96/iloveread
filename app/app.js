var express=require('express')
var app=express();
var http = require('http').Server(app);
var bodyParser=require('body-parser');
app.use(express.static("public"));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.get('/',function(req,res){
     res.sendFile(__dirname+'public/index.html');
 });

app.use('/rest',require('./routes/rest'));

http.listen(8080,function(){
    console.log("server listening on port 8080");
});

//https://docs.nodejitsu.com/articles/HTTP/clients/how-to-create-a-HTTP-request/