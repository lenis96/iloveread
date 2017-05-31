var exports = module.exports = {};
exports.getConnection=function(f){
	var mysql=require('mysql');
	var connection=mysql.createConnection({
		host:'localhost',
		user:'lenis96',
		password:'',
		database:'iloveread'
	});
	connection.connect();
	f(connection);
	connection.end();
}
exports.getConnection2=function(f){
	var mysql=require('mysql');
	var connection=mysql.createConnection({
		host:'localhost',
		user:'lenis96',
		password:'',
		database:'iloveread'
	});
	connection.connect();
	f(connection);
	//connection.end();
}