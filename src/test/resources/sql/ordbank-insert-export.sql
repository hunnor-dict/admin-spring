TRUNCATE TABLE BOYING;
TRUNCATE TABLE BOYING_GRUPPER;
TRUNCATE TABLE FULLFORMSLISTE;
TRUNCATE TABLE LEMMA;
TRUNCATE TABLE LEMMA_PARADIGME;
TRUNCATE TABLE PARADIGME;
TRUNCATE TABLE PARADIGME_BOYING;

INSERT INTO LEMMA (LEMMA_ID, GRUNNFORM) VALUES (1, 'bil');
INSERT INTO LEMMA_PARADIGME (LEMMA_ID, PARADIGME_ID) VALUES (1, '700');

INSERT INTO LEMMA (LEMMA_ID, GRUNNFORM) VALUES (2, 'jente');
INSERT INTO LEMMA_PARADIGME (LEMMA_ID, PARADIGME_ID) VALUES (2, '702');
INSERT INTO LEMMA_PARADIGME (LEMMA_ID, PARADIGME_ID) VALUES (2, '902');

INSERT INTO LEMMA (LEMMA_ID, GRUNNFORM) VALUES (3, 'ferge');

INSERT INTO LEMMA (LEMMA_ID, GRUNNFORM) VALUES (4, 'ferje');

INSERT INTO LEMMA (LEMMA_ID, GRUNNFORM) VALUES (5, 'ferge');
INSERT INTO LEMMA_PARADIGME (LEMMA_ID, PARADIGME_ID) VALUES (5, '001');
INSERT INTO LEMMA_PARADIGME (LEMMA_ID, PARADIGME_ID) VALUES (5, '011');

INSERT INTO LEMMA (LEMMA_ID, GRUNNFORM) VALUES (6, 'ferje');
INSERT INTO LEMMA_PARADIGME (LEMMA_ID, PARADIGME_ID) VALUES (6, '001');
INSERT INTO LEMMA_PARADIGME (LEMMA_ID, PARADIGME_ID) VALUES (6, '011');

INSERT INTO LEMMA (LEMMA_ID, GRUNNFORM) VALUES (8, 'forsvarsdepartement');
INSERT INTO LEMMA_PARADIGME (LEMMA_ID, PARADIGME_ID) VALUES (8, '800');

INSERT INTO LEMMA (LEMMA_ID, GRUNNFORM) VALUES (9, 'rask');
INSERT INTO LEMMA_PARADIGME (LEMMA_ID, PARADIGME_ID) VALUES (9, '500');

INSERT INTO LEMMA (LEMMA_ID, GRUNNFORM) VALUES (10, 'raskt');

INSERT INTO LEMMA (LEMMA_ID, GRUNNFORM) VALUES (11, 'fort');
INSERT INTO LEMMA_PARADIGME (LEMMA_ID, PARADIGME_ID) VALUES (11, '695');

INSERT INTO PARADIGME (PARADIGME_ID, BOY_GRUPPE, ORDKLASSE, ORDKLASSE_UTDYPING, FORKLARING, DOEME, ID)
	VALUES ('001', '', '', '', '', '', 1);
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('001', 1, '', 'e');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('001', 2, '', 'er');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('001', 3, '', 'es');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('001', 4, '', 'a');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('001', 4, '', 'a');

INSERT INTO PARADIGME (PARADIGME_ID, BOY_GRUPPE, ORDKLASSE, ORDKLASSE_UTDYPING, FORKLARING, DOEME, ID)
	VALUES ('011', '', '', '', '', '', 1);
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('011', 1, '', 'e');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('011', 2, '', 'er');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('011', 3, '', 'es');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('011', 4, '', 'a');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('011', 4, '', 'a');

INSERT INTO PARADIGME (PARADIGME_ID, BOY_GRUPPE, ORDKLASSE, ORDKLASSE_UTDYPING, FORKLARING, DOEME, ID)
	VALUES ('500', '', '', '', '', '', 1);
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('500', 1, '', '');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('500', 2, '', 'e');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('500', 3, '', 'e');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('500', 4, '', 't');

INSERT INTO PARADIGME (PARADIGME_ID, BOY_GRUPPE, ORDKLASSE, ORDKLASSE_UTDYPING, FORKLARING, DOEME, ID)
	VALUES ('695', '', '', '', '', '', 1);
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('695', 1, '', '');

INSERT INTO PARADIGME (PARADIGME_ID, BOY_GRUPPE, ORDKLASSE, ORDKLASSE_UTDYPING, FORKLARING, DOEME, ID)
	VALUES ('700', '', '', '', '', '', 1);
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('700', 1, '', '');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('700', 2, '', 'en');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('700', 3, '', 'er');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('700', 4, '', 'ene');

INSERT INTO PARADIGME (PARADIGME_ID, BOY_GRUPPE, ORDKLASSE, ORDKLASSE_UTDYPING, FORKLARING, DOEME, ID)
	VALUES ('702', '', '', '', '', '', 1);
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('702', 1, '', 'e');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('702', 2, '', 'en');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('702', 3, '', 'er');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('702', 4, '', 'ene');

INSERT INTO PARADIGME (PARADIGME_ID, BOY_GRUPPE, ORDKLASSE, ORDKLASSE_UTDYPING, FORKLARING, DOEME, ID)
	VALUES ('800', '', '', '', '', '', 1);
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('800', 1, '', '');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('800', 2, '', 'et');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('800', 3, '', '');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('800', 4, '', 'a');

INSERT INTO PARADIGME (PARADIGME_ID, BOY_GRUPPE, ORDKLASSE, ORDKLASSE_UTDYPING, FORKLARING, DOEME, ID)
	VALUES ('902', '', '', '', '', '', 1);
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('902', 1, '', 'e');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('902', 2, '', 'a');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('902', 3, '', 'er');
INSERT INTO PARADIGME_BOYING (PARADIGME_ID, BOY_NUMMER, BOY_GRUPPE, BOY_UTTRYKK) VALUES ('902', 4, '', 'ene');
