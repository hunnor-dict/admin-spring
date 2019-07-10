TRUNCATE TABLE HN_HU_ENTRY;
TRUNCATE TABLE HN_HU_LEMMA;
TRUNCATE TABLE HN_HU_ENTRY_LEMMA;

INSERT INTO HN_HU_ENTRY (ENTRY_ID, POS, STATUS, TRANSLATION) VALUES (1, 'fn', 1, '<senseGrp><sense></sense></senseGrp>');
INSERT INTO HN_HU_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (1, 1, NULL, 0, 0, 1);
INSERT INTO HN_HU_LEMMA (LEMMA_ID, GRUNNFORM) VALUES (1, 'alma');

INSERT INTO HN_HU_ENTRY (ENTRY_ID, POS, STATUS, TRANSLATION) VALUES (2, 'fn', 1, '<senseGrp><sense></sense></senseGrp>');
INSERT INTO HN_HU_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (2, 2, NULL, 0, 0, 1);
INSERT INTO HN_HU_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (2, 3, NULL, 0, 1, 1);
INSERT INTO HN_HU_LEMMA (LEMMA_ID, GRUNNFORM) VALUES (2, 'tejföl');
INSERT INTO HN_HU_LEMMA (LEMMA_ID, GRUNNFORM) VALUES (3, 'tejfel');

TRUNCATE TABLE HN_NB_ENTRY;
TRUNCATE TABLE HN_NB_LEMMA;
TRUNCATE TABLE HN_NB_ENTRY_LEMMA;
TRUNCATE TABLE HN_NB_LEMMA_PARADIGME;

INSERT INTO HN_NB_ENTRY (ENTRY_ID, POS, STATUS, TRANSLATION) VALUES (1, 'subst', 1, '<senseGrp><sense></sense></senseGrp>');
INSERT INTO HN_NB_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (1, 1, NULL, 0, 0, 0);

INSERT INTO HN_NB_ENTRY (ENTRY_ID, POS, STATUS, TRANSLATION) VALUES (2, 'subst', 1, '<senseGrp><sense><lbl lang="hun">hun</lbl></sense></senseGrp>');
INSERT INTO HN_NB_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (2, 2, NULL, 0, 0, 0);

INSERT INTO HN_NB_ENTRY (ENTRY_ID, POS, STATUS, TRANSLATION) VALUES (3, 'subst', 1, '<!-- WIP --><senseGrp><sense></sense></senseGrp>');
INSERT INTO HN_NB_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (3, 3, NULL, 0, 0, 0);
INSERT INTO HN_NB_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (3, 4, NULL, 0, 1, 0);

INSERT INTO HN_NB_ENTRY (ENTRY_ID, POS, STATUS, TRANSLATION) VALUES (5, 'verb', 1, '<senseGrp><sense></sense></senseGrp>');
INSERT INTO HN_NB_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (5, 5, NULL, 0, 0, 0);
INSERT INTO HN_NB_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (5, 6, NULL, 0, 1, 0);

INSERT INTO HN_NB_ENTRY (ENTRY_ID, POS, STATUS, TRANSLATION) VALUES (7, 'subst', 1, '<senseGrp><sense></sense></senseGrp>');
INSERT INTO HN_NB_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (7, 7, NULL, 0, 0, 1);
INSERT INTO HN_NB_LEMMA (LEMMA_ID, GRUNNFORM) VALUES (7, 'brannbil');
INSERT INTO HN_NB_LEMMA_PARADIGME (LEMMA_ID, PARADIGME_ID) VALUES (7, '7AB');

INSERT INTO HN_NB_ENTRY (ENTRY_ID, POS, STATUS, TRANSLATION) VALUES (8, 'subst', 1, '<senseGrp><sense></sense></senseGrp>');
INSERT INTO HN_NB_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (8, 8, '800', 2, 0, 0);

INSERT INTO HN_NB_ENTRY (ENTRY_ID, POS, STATUS, TRANSLATION) VALUES (9, 'adj', 1, '<senseGrp><sense></sense></senseGrp>');
INSERT INTO HN_NB_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (9, 9, NULL, 0, 0, 0);

INSERT INTO HN_NB_ENTRY (ENTRY_ID, POS, STATUS, TRANSLATION) VALUES (10, 'adv', 1, '<senseGrp><sense></sense></senseGrp>');
INSERT INTO HN_NB_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (10, 10, '', 0, 0, 0);

INSERT INTO HN_NB_ENTRY (ENTRY_ID, POS, STATUS, TRANSLATION) VALUES (11, 'adv', 1, '<senseGrp><sense></sense></senseGrp>');
INSERT INTO HN_NB_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (11, 11, NULL, 0, 0, 0);

INSERT INTO HN_NB_ENTRY (ENTRY_ID, POS, STATUS, TRANSLATION) VALUES (12, 'verb', 1, '<senseGrp><sense></sense></senseGrp>');
INSERT INTO HN_NB_ENTRY_LEMMA (ENTRY_ID, LEMMA_ID, PARADIGME_ID, BOY_NUMMER, WEIGHT, SOURCE) VALUES (12, 12, NULL, 0, 0, 0);

-- Entry without lemma
INSERT INTO HN_NB_ENTRY (ENTRY_ID, POS, STATUS, TRANSLATION) VALUES (99, 'subst', 1, '<senseGrp><sense></sense></senseGrp>');
