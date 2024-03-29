-- Database Name:
CREATE DATABASE user_manager_v1;

USE user_manager_v1;

GRANT ALL PRIVILEGES ON user_manager_v1.* TO 'Jayson'@'localhost' IDENTIFIED BY 'jayson123';


-- USER TABLE STRUCTURE:
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

use proyecto;

insert into users (user_id, first_name, last_name, email, password, created_at, updated_at)
values (1, 'michael', 'paliz', 'michelpaliz@hotmail.com', '1234', null, null);


# ALTER TABLE users
#     ADD type boolean NULL;
#
# ALTER TABLE users
#     DROP COLUMN type;


# *************** WE CREATE THE OTHER TABLES *************;

CREATE SCHEMA IF NOT EXISTS proyecto DEFAULT CHARACTER SET utf8;
USE proyecto;

-- -----------------------------------------------------
-- Table `mydb`.`autor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS proyecto.`autor`
(
    `autor_id`   INT         NOT NULL AUTO_INCREMENT,
    `muerte`     INT         NULL,
    `nacimiento` INT         NULL,
    `nombre`     VARCHAR(45) NULL,
    `profesion`  VARCHAR(45) NULL,
    PRIMARY KEY (`autor_id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS proyecto.`categoria`
(
    `categoria_id` INT         NOT NULL AUTO_INCREMENT,
    `nombre`       VARCHAR(45) NULL,
    PRIMARY KEY (`categoria_id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Frase`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS proyecto.`frase`
(
    `frase_id`        INT         NOT NULL AUTO_INCREMENT,
    `fecha_programada` VARCHAR(45) NULL,
    `texto`           VARCHAR(200) NULL,
    `autor_id`        INT         NOT NULL,
    `categoria_id`    INT         NOT NULL,
    PRIMARY KEY (`frase_id`),
    INDEX `fk_Frase_autor_idx` (`autor_id` ASC) VISIBLE,
    INDEX `fk_Frase_categoria1_idx` (`categoria_id` ASC) VISIBLE,
    CONSTRAINT `fk_Frase_autor`
        FOREIGN KEY (`autor_id`)
            REFERENCES proyecto.`autor` (`autor_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_Frase_categoria1`
        FOREIGN KEY (`categoria_id`)
            REFERENCES proyecto.`categoria` (`categoria_id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;



# NOW WE CHARGE THE DATA THAT WE HAVE TO TEST OUR APP

INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1600, '1500', 'Satoshi Nakamoto', 'Escritor');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1700, '1600', 'Rabin Menisha', 'Jardinero');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1850, '1800', 'Emily Dickinson', 'Poetista');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1900, '1800', 'Alberto Mendoza', 'Peón de obra');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1957, '1885', 'Sacha Guitry', 'Director, actor y guionista');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1890, '1801', 'John H. Newman', 'Cardenal');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1677, '1632', 'Baruch Spinoza', 'Filósofo');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1883, '1818', 'Karl Marx', 'Filósofo y economista');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1871, '1793', 'Charles Paul de Kock', 'Escritor');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1645, '1580', 'Francisco de Quevedo', 'Escritor');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1832, '1749', 'Goethe', 'Poeta y dramaturgo');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1967, '1885', 'André Mautois', 'Novelista');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1962, '1926', 'Marilyn Monroe', 'Actriz');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (-322, '-384', 'Aristóteles', 'Filósofo griego');
INSERT INTO autor (muerte, nacimiento, nombre, profesion) VALUES (1955, '1955', 'Albert Einstein', 'Científico');

INSERT INTO categoria (nombre) VALUES ('Infancia');
INSERT INTO categoria (nombre) VALUES ('Sentimientos');
INSERT INTO categoria (nombre) VALUES ('Naturaleza');
INSERT INTO categoria (nombre) VALUES ('Felicidad');
INSERT INTO categoria (nombre) VALUES ('Sociedad');
INSERT INTO categoria (nombre) VALUES ('Libertad');
INSERT INTO categoria (nombre) VALUES ('Respeto');
INSERT INTO categoria (nombre) VALUES ('Miscelánea');
INSERT INTO categoria (nombre) VALUES ('Ser humano');
INSERT INTO categoria (nombre) VALUES ('Amor');
INSERT INTO categoria (nombre) VALUES ('Cambio');
INSERT INTO categoria (nombre) VALUES ('Exito');
INSERT INTO categoria (nombre) VALUES ('Ignorancia');

INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-01', 'La esperanza es el sueño del hombre despierto', 14, 9);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-02', 'Todos somos muy ignorantes. Lo que ocurre es que no todos ignoramos las mismas cosas', 15, 13);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-03', 'El sabio no dice todo lo que piensa, pero siempre piensa todo lo que dice', 14, 9);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-04', 'Nada nos puede impedir sentir esta maravillosa felicidad de ser preferidos a otros.', 4, 4);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-05', 'Aquellos que niegan la libertad a otros no la merecen para sí, y bajo un Dios justo no pueden conservarla mucho tiempo.', 5, 5);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-06', 'Las cadenas de la esclavitud solamente atan las manos: es la mente lo que hace al hombre libre o esclavo.', 6, 6);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-07', 'No hay nada más despreciable que el respeto basado en el miedo.', 7, 7);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-08', 'Siempre es más valioso tener el respeto que la admiración de las personas.', 8, 8);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-09', 'Siempre me ha parecido que a un ser humano sólo le puede salvar otro ser humano.', 9, 9);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-10', 'Ama y haz lo que quieras. Si callas, callarás con amor; si gritas, gritarás con amor; si corriges, corregirás con amor, si perdonas, perdonarás con amor.', 10, 10);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-11', 'Es el cambio, el cambio continuo, el cambio inevitable, el factor dominante de la sociedad actual.', 11, 11);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-12', 'Si nunca has tenido un gran éxito, no sabes lo que vales; el éxito es la piedra de toque de los caracteres.', 12, 12);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-13', 'El sexo forma parte de la naturaleza. Y yo me llevo de maravilla con la naturaleza.', 13, 3);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-14', 'El sexo forma parte de la naturaleza. Y yo me llevo de maravilla con la naturaleza.', 12, 11);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-15', 'El amor sólo da de sí y nada recibe sino de sí mismo. El amor no posee, y no quiere ser poseído. Porque al amor le basta con el amor.', 11, 10);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-16', 'El cuerpo humano es el carruaje; el yo, el hombre que lo conduce; el pensamiento son las riendas, y los sentimientos los caballos.', 10, 9);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-17', 'Con la primera copa el hombre bebe vino; con la segunda el vino bebe vino, y con la tercera, el vino bebe al hombre.', 9, 8);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-18', 'El respeto de sí mismo es, después de la religión, el principal freno de los vicios.', 8, 7);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-19', 'No puedes separar la paz de la libertad, porque nadie puede estar en paz, a no ser que tenga su libertad.', 7, 6);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-20', 'Una sociedad se embrutece más con el empleo habitual de los castigos que con la repetición de los delitos.', 6, 5);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-21', 'Es muy difícil hacer bella la felicidad. Una felicidad que sólo es ausencia de desdicha es cosa fea.', 5, 4);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-22', 'La naturaleza humana es buena y la maldad es esencialmente antinatural.', 4, 3);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-23', 'Los sentimientos delicados que nos dan la vida yacen entumecidos en la mundanal confusión.', 3, 2);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-24', 'Siempre hay un momento en la infancia en el que se abre una puerta y deja entrar al futuro.', 2, 1);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-25', 'La infancia tiene sus propias maneras de ver, pensar y sentir; nada hay más insensato que pretender sustituirlas por las nuestras.', 1, 10);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-26', 'Después de aquellos que ocupan los primeros puestos, no conozco a nadie tan desgraciado como quien los envidia.', 1, 1);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-27', 'Los niños adivinan qué personas los aman. Es un don natural que con el tiempo se pierde.', 2, 2);
INSERT INTO frase (fecha_programada, texto, autor_id, categoria_id) VALUES ('2023-02-28', 'Por nuestra codicia lo mucho es poco; por nuestra necesidad lo poco es mucho.', 3, 3);

