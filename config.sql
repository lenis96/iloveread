drop database if exists iloveread;
create database iloveread;
use iloveread
drop table if exists book;
drop table if exists author;
drop table if exists categorie;
drop table if exists user;
drop table if exists city;
drop table if exists sex;
drop table if exists country;

create table country(
idCountry int primary key,
description varchar(50)
);

create table city(
idCity int primary key,
description varchar(50),
idCountry int,
foreign key (idCountry) references country(idCountry)
);

create table sex(
idSex int primary key,
description varchar(50)
);

create table user (
user varchar(50) primary key,
password varchar(50),
name varchar(50),
lastname varchar(50),
idSex int,
idcity int,
birthDate date,
address varchar(50),
email varchar(50),
foreign key (idSex) references sex(idSex),
foreign key (idCity) references city(idCity)
);

create table categorie(
idCategorie int primary key,
description varchar(50)
);

create table author(
idAuthor int primary key,
name varchar(50),
lastname varchar(50)
);

create table book(
isbn int primary key,
title varchar(50),
idAuthor int,
abstract text,
idCategorie int,
dateRegister date,
price int,
imgSrc varchar(50),
compras int,
pdfsrc varchar(50),
foreign key (idAuthor) references author(idAuthor),
foreign key (idCategorie) references categorie(idCategorie)
);
create table compras(
isbn int,
usuario varchar(50),
precio int,
fechaCompra timestamp,
foreign key (isbn) references book(isbn),
foreign key (usuario) references user(user)
);


create table carritoCompras(
isbn int,
usuario varchar(50),
foreign key (isbn) references book(isbn),
foreign key (usuario) references user(user)
);

create table listaDeseos(
isbn int,
usuario varchar(50),
foreign key (isbn) references book(isbn),
foreign key (usuario) references user(user)
);

insert into country values (1,'Colombia');
insert into city values(1,'Cali',1);
insert into sex values(1,'Masculino');
insert into sex values(2,'Femenino');
insert into user values('lenis','1234','Andres','Lenis',1,1,'1996-06-7','calle 5','lenisandres5@gmail.com');
insert into categorie values(1,'Ciencia');
insert into categorie values(2,'Literatura');
insert into categorie values(3,'Filosofia');
insert into author values(1,'Victor Hugo',null);
insert into author values(2,'Charles Darwin',null);
insert into author values(3,'Nicolas Maquiavelo',null);
insert into author values(4,'Antoine de Saint-Exupéry',null);
insert into book values (2,'El origen de las especies',2,'',1,'2017-05-17',15000,'elOrigenDeLasEspecies.jpg',0,'elOrigenDeLasEspecies.jpg');
insert into book values(1,'Los miserables',1,'Una muy buena obra',2,'2017-05-11',28000,'losMiserables.jpg',0,'losMiserables.jpg');
insert into book values(3,'El principe',3,'Una muy buena obra',3,'2017-05-16',29500,'el-principe.jpg',0,'el-principe.jpg');
insert into book values(4,'El principito',4,'Una muy buena obra',2,'2017-05-16',29500,'elPrincipito.jpg',0,'elPrincipito.jpg');
DELIMITER $$
CREATE PROCEDURE `getUserDetail`(
  in userIn varchar(50),
  passwordIn varchar(50)
)
BEGIN
  SELECT user.name,user.lastname,user.email FROM user join city on(password=passwordIn and user=userIn and user.idcity=city.idcity) join country on(city.idCountry=country.idCountry) ;
END;

Create procedure getCategories(
)
BEGIN
  select * from categorie;
END;

Create procedure getLibros(
  in categorieIn int,
  in cantidadIn int
)
BEGIN
  IF categorieIn=0 then
    select isbn,title as titulo,author.name as autor,price as precio,concat('img/',book.imgSrc) as imgSrc from book join categorie on(book.idCategorie=categorie.idCategorie) join author on(book.idAuthor=author.idAuthor);
  ELSE
    select isbn,title as titulo,author.name as autor,price as precio,concat('img/',book.imgSrc) as imgSrc from book join author on(book.idAuthor=author.idAuthor)  where(book.idCategorie=categorieIn);
  END IF;
  
END;

create procedure obtenerLibro(
in isbnIn int,
in userIn varchar(50))
begin
select book.isbn as idLibro,title as titulo,author.name as autor,price as precio,categorie.description as categoria,book.abstract as descripcion,carritoCompras.isbn as carrito,concat('img/',book.imgSrc) as imgSrc from book join categorie on(book.idCategorie=categorie.idCategorie) join author on(book.idAuthor=author.idAuthor) left join carritoCompras on(book.isbn=carritoCompras.isbn and carritoCompras.usuario=userIn) where book.isbn=isbnIn limit 1;
end;
create procedure getNuevos()
begin
  select isbn,title as titulo,author.name as autor,price as precio,concat('img/',book.imgSrc) as imgSrc from book join author on(book.idAuthor=author.idAuthor) order by dateRegister desc limit 10;
end;
create procedure getMasVendidos()
begin
  select isbn,title as titulo,author.name as autor,price as precio,concat('img/',book.imgSrc) as imgSrc from book join author on(book.idAuthor=author.idAuthor) order by compras desc limit 10;
END;
create procedure getHome()
begin
  call getNuevos();
  call getMasVendidos();
end;

Create procedure comprarLibro(
  in isbnIn int,
  in usuarioIn varchar(50)
)
BEGIN
  insert into compras values(isbnIn,usuarioIn,(select price from book where isbn=isbnIn),CURRENT_TIMESTAMP);
  
END;

Create procedure agregarCarroCompras(
  in isbnIn int,
  in usuarioIn varchar(50)
)
BEGIN
  insert into carritoCompras values(isbnIn,usuarioIn);
  
END;
Create procedure agregarListaDeseos(
  in isbnIn int,
  in usuarioIn varchar(50)
)
BEGIN
  insert into listaDeseos values(isbnIn,usuarioIn);
  
END;

Create procedure eliminarCarroCompras(
  in isbnIn int,
  in usuarioIn varchar(50)
)
BEGIN
  delete from carritoCompras where usuario=usuarioIn and isbn=isbnIn;
  
END;

create procedure obtenerCarritoCompras(
in usuarioIn varchar(50)
)
begin
  select book.isbn as id,title as titulo,name as autor, price as precio,concat('img/',book.imgSrc) as imgSrc from carritoCompras join book on(book.isbn=carritoCompras.isbn and carritoCompras.usuario=usuarioIn) join author on(author.idAuthor=book.idAuthor);
end;

Create procedure eliminarListaDeseos(
  in isbnIn int,
  in usuarioIn varchar(50)
)
BEGIN
  delete from listaDeseos where usuario=usuarioIn and isbn=isbnIn;
  
END;

Create procedure comprarLibros(
  in usuarioIn varchar(50)
)

BEGIN

declare isbnIn int;
declare precioIn int;
declare ahora timestamp;
declare done int;
declare cur cursor for select carritoCompras.isbn, price from carritoCompras join book on(book.isbn=carritoCompras.isbn and carritoCompras.usuario=usuarioIn) ;
declare continue handler for not found set done=1;
select email from user where user=usuarioIn;
select carritoCompras.isbn, price,book.title,book.pdfsrc from carritoCompras join book on(book.isbn=carritoCompras.isbn and carritoCompras.usuario=usuarioIn);
set ahora= CURRENT_TIMESTAMP;
open cur;
a: loop
  fetch cur into isbnIn,precioIn;
  if done = 1 then leave a; end if;
  insert into compras values(isbnIn,usuarioIn,precioIn,ahora);
  update book set compras=compras+1 where isbn=isbnIn;
end loop a;
close cur;
delete from carritoCompras where usuario=usuarioIn;
  
END;


create procedure actulizarUsuario(
userIn varchar(50),
passwordIn varchar(50),
nameIn varchar(50),
lastnameIn varchar(50),
idSexIn int,
idcityIn int,
birthDateIn date,
addressIn varchar(50)
)
begin
update user set name=nameIn,lastname=lastnameIn,idSex=idSexIn,idCity=idCityIn,birthDate=birthDateIn,address=addressIn where user=userIn;
end;

create procedure crearUsuario(
userIn varchar(50),
passwordIn varchar(50),
nameIn varchar(50),
lastnameIn varchar(50),
idSexIn int,
idcityIn int,
birthDateIn date,
addressIn varchar(50)
)
begin
insert into user values(userIn,passwordIn,nameIn,lastnameIn,idSexIn,idCityIn,birthDateIn,addressIn);
end;
create procedure eliminarLibroCarrito(
userIn varchar(50),
isbnIn int
)
begin
delete from carritoCompras where usuario=userIn and isbn=isbnIn;
end;
create procedure eliminarCarritoCompras(
userIn varchar(50)
)
begin
delete from carritoCompras where usuario=userIn;
end;
create procedure buscar(
busq varchar(50))
begin
select isbn,title as titulo,author.name as autor,price as precio,concat('img/',book.imgSrc) as imgSrc  from book join author on(book.idAuthor=author.idAuthor) where title like concat('%',busq,'%') or name like concat('%',busq,'%');
end;
create procedure actualizarE(
userIn varchar(50),
emailIn varchar(50)
)
begin
update user set email=emailIn where user=userIn;
end;
$$
DELIMITER  ; $$




