--Creare la tabella stati


CREATE SEQUENCE sequence_country START 1;
CREATE TABLE countries (
id integer DEFAULT nextval('sequence_country'),
name varchar(30),
PRIMARY KEY (id));


--Creare la tabella linguaggi

CREATE SEQUENCE sequence_languages START 1;
CREATE TABLE languages (
id integer DEFAULT nextval('sequence_languages'),
name varchar(30),
PRIMARY KEY (id));



--Creare la tabella punteggio


CREATE SEQUENCE sequence_score START 1;
CREATE TABLE score (
id integer DEFAULT nextval('sequence_score'),
id_country integer,
id_language integer,
score integer,
month date, 					--first day of the month
PRIMARY KEY (id_country, id_language),
FOREIGN KEY(id_country) REFERENCES countries(id),
FOREIGN KEY(id_language) REFERENCES languages(id));
