--Create table countries

CREATE SEQUENCE sequence_countries START 1;

<<<<<<< HEAD
CREATE TABLE countries (
	id integer DEFAULT nextval('sequence_countries'),
	name varchar(30),
	id_state_twitter varchar(17),
	PRIMARY KEY (id)
);
=======
CREATE SEQUENCE sequence_country START 1;
CREATE TABLE countries (
id integer DEFAULT nextval('sequence_country'),
name varchar(30),
PRIMARY KEY (id));
>>>>>>> HomepageWebsite

--Create table languages

CREATE SEQUENCE sequence_languages START 1;
<<<<<<< HEAD

CREATE TABLE languages (
	id integer DEFAULT nextval('sequence_languages'),
	name varchar(30),
	tags varchar(200),
	PRIMARY KEY (id)
);

--Create table scores

CREATE SEQUENCE sequence_scores START 1;

CREATE TABLE scores (
	id integer DEFAULT nextval('sequence_scores'),
	id_country integer,
	id_language integer,
	score integer,
	month date, 					--first day of the month
	PRIMARY KEY (id),
	FOREIGN KEY(id_country) REFERENCES countries(id),
	FOREIGN KEY(id_language) REFERENCES languages(id)
);

--Create costraint on id_country and id_languages
CREATE UNIQUE INDEX constraint_lang_ctr ON scores (id_country, id_language);
=======
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
>>>>>>> HomepageWebsite
