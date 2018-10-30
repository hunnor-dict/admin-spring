TRUNCATE TABLE hn_hun_segment;
TRUNCATE TABLE hn_hun_tr_nob_tmp;

INSERT INTO hn_hun_segment (id, entry, orth, pos, par, seq, status) VALUES (1, 1, 'alma', 'subst', '0', 1, 1);
INSERT INTO hn_hun_tr_nob_tmp (id, trans) VALUES (1, '<senseGrp/>');

INSERT INTO hn_hun_segment (id, entry, orth, pos, par, seq, status) VALUES (2, 2, 'tejföl', 'subst', '0', 1, 1);
INSERT INTO hn_hun_segment (id, entry, orth, pos, par, seq, status) VALUES (3, 2, 'tejfel', 'subst', '0', 1, 1);
INSERT INTO hn_hun_tr_nob_tmp (id, trans) VALUES (2, '<senseGrp/>');

-- Database inconsistencies in v2
INSERT INTO hn_hun_tr_nob_tmp (id, trans) VALUES (99, '<senseGrp/>');

TRUNCATE TABLE hn_nob_segment;
TRUNCATE TABLE hn_nob_tr_hun_tmp;

INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (1, 1, 'bil', 'subst', '700', 1, 1);
INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (1, 1, 'bilen', 'subst', '700', 2, 1);
INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (1, 1, 'biler', 'subst', '700', 3, 1);
INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (1, 1, 'bilene', 'subst', '700', 4, 1);
INSERT INTO hn_nob_tr_hun_tmp (id, trans) VALUES (1, '<senseGrp/>');

INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (2, 2, 'brannbil', 'subst', '700', 1, 1);
INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (2, 2, 'brannbilen', 'subst', '700', 2, 1);
INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (2, 2, 'brannbiler', 'subst', '700', 3, 1);
INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (2, 2, 'brannbilene', 'subst', '700', 4, 1);
INSERT INTO hn_nob_tr_hun_tmp (id, trans) VALUES (2, '<senseGrp/>');

INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (3, 3, 'lastebil', 'subst', '0', 1, 1);
INSERT INTO hn_nob_tr_hun_tmp (id, trans) VALUES (3, '<senseGrp/>');

INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (4, 4, 'skrive', 'verb', '001', 1, 1);
INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (4, 4, 'skrivende', 'adj', '001', 2, 1);
INSERT INTO hn_nob_tr_hun_tmp (id, trans) VALUES (4, '<senseGrp/>');

INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (5, 5, 'band', 'subst', '800', 1, 1);
INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (6, 5, 'bånd', 'subst', '800', 1, 1);
INSERT INTO hn_nob_tr_hun_tmp (id, trans) VALUES (5, '<senseGrp/>');

INSERT INTO hn_nob_segment (id, entry, orth, pos, par, seq, status) VALUES (2310, 2310, 'annetstedsfra', 'adv', '695', 1, 1);
INSERT INTO hn_nob_tr_hun_tmp (id, trans) VALUES (2310, '<senseGrp/>');

-- Database inconsistencies in v2
INSERT INTO hn_nob_tr_hun_tmp (id, trans) VALUES (99, '<senseGrp/>');
