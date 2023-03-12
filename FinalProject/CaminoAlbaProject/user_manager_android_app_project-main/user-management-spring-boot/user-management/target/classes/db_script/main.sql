
use CaminoAlba;

CREATE TABLE users
(
    user_id    INT          NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    verification_code varchar(100) null,
    type       varchar(10) not null default 'user',
    enabled bit NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id)
);






insert into users (user_id, email, first_name,last_name,password,type)
values (1,'michaelpaliz@hotmail.com','michael', 'paliz', '1234','admin');

insert into profile (first_name,  last_name, birth_date, gender ,photo)
values ('michael', 'paliz', '1234','admin');