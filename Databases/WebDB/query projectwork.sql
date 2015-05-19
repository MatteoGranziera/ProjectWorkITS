--Creare la tabella stati


CREATE SEQUENCE sequence_country START 1;
CREATE TABLE country (
id integer,
nome varchar(30),
PRIMARY KEY (id));


--Creare la tabella linguaggi

CREATE SEQUENCE sequence_languages START 1;
CREATE TABLE languages (
id integer,
nome varchar(30),
PRIMARY KEY (id));



--Creare la tabella punteggio


CREATE SEQUENCE sequence_score START 1;
CREATE TABLE score (
id_country integer,
id_language integer,
score integer,
month date, 					--first day of the month
PRIMARY KEY (id_country, id_language),
FOREIGN KEY(id_country) REFERENCES country(id),
FOREIGN KEY(id_language) REFERENCES languages(id));
