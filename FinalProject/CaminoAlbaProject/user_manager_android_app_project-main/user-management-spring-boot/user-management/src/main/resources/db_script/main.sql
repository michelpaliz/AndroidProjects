
use CaminoAlba;

CREATE TABLE users
(
    user_id    INT          NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    type       varchar(10),
    PRIMARY KEY (user_id)
);






insert into users (user_id, email, first_name,last_name,password,type)
values (1,'michaelpaliz@hotmail.com','michael', 'paliz', '1234','admin');

insert into person (first_name,last_name,password,type)
values ('michael', 'paliz', '1234','admin');