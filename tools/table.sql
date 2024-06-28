CREATE DATABASE btp;

\c btp

CREATE TABLE admin (
    id serial primary key,
    username VARCHAR,
    password VARCHAR,
    profil VARCHAR
);

CREATE SEQUENCE seq_client START WITH 1;
CREATE TABLE client (
    id VARCHAR(10) DEFAULT (LEFT('CLI',3)||LPAD(nextval('seq_client')::text, 5,'0')),
    username VARCHAR UNIQUE,
    profil VARCHAR,
    primary key(id) 
);
ALTER TABLE client ALTER column profil SET DEFAULT 'user';

CREATE SEQUENCE seq_maison START WITH 1;
CREATE TABLE maison (
    id VARCHAR(10) DEFAULT (LEFT('MAI',3)||LPAD(nextval('seq_maison')::text, 5,'0')),
    nom VARCHAR,
    primary key(id)
);
ALTER TABLE maison ADD column prix numeric;
ALTER TABLE maison ALTER column prix SET DEFAULT 0;

CREATE SEQUENCE seq_desc_maison START WITH 1;
CREATE TABLE desc_maison (
    id VARCHAR(10) DEFAULT (LEFT('DSC',3)||LPAD(nextval('seq_desc_maison')::text, 5,'0')),
    idmaison VARCHAR,
    description VARCHAR,
    primary key(id),
    foreign key (idmaison) references maison (id)
);
ALTER TABLE desc_maison add column surface numeric;

CREATE SEQUENCE seq_finition START WITH 1;
CREATE TABLE finition (
    id VARCHAR(10) DEFAULT (LEFT('FIN',3)||LPAD(nextval('seq_finition')::text, 5,'0')),
    nom VARCHAR,
    pourcentage numeric,
    primary key(id)
);

CREATE SEQUENCE seq_duree START WITH 1;
CREATE TABLE duree (
    id VARCHAR(10) DEFAULT (LEFT('DUR',3)||LPAD(nextval('seq_duree')::text, 5,'0')),
    idmaison VARCHAR,
    valeur int,
    primary key(id),
    foreign key (idmaison) references maison (id)
);


CREATE SEQUENCE seq_devis START WITH 1;
CREATE TABLE devis (
    id VARCHAR(10) DEFAULT (LEFT('DEV',3)||LPAD(nextval('seq_devis')::text, 5,'0')),
    idclient VARCHAR,
    idmaison VARCHAR,
    idfinition VARCHAR,
    date date,
    dateDebut date,
    prix numeric,
    primary key(id),
    foreign key (idmaison) references maison (id),
    foreign key (idclient) references client (id),
    foreign key (idfinition) references finition (id)
);
alter table devis add column datefin date;
alter table devis add column lieu VARCHAR;

CREATE SEQUENCE seq_paiement START WITH 1;
CREATE TABLE paiement (
    id VARCHAR(10) DEFAULT (LEFT('PAI',3)||LPAD(nextval('seq_paiement')::text, 5,'0')),
    iddevis VARCHAR,
    montant numeric,
    date date,
    primary key(id),
    foreign key (iddevis) references devis (id)
);
CREATE SEQUENCE seq_ref START WITH 1;
alter table paiement add column ref VARCHAR DEFAULT  (LEFT('REF',3)||LPAD(nextval('seq_ref')::text, 5,'0'));

CREATE SEQUENCE seq_travaux START WITH 1;
CREATE TABLE travaux (
    id VARCHAR(10) DEFAULT (LEFT('TRA',3)||LPAD(nextval('seq_travaux')::text, 5,'0')),
    nom VARCHAR,
    primary key(id)
);

CREATE SEQUENCE seq_unite START WITH 1;
CREATE TABLE unite (
    id VARCHAR(10) DEFAULT (LEFT('UNI',3)||LPAD(nextval('seq_unite')::text, 5,'0')),
    nom VARCHAR,
    equivalence numeric,
    primary key(id)
);

CREATE SEQUENCE seq_type_travaux START WITH 1;
CREATE TABLE typeTravaux (
    id VARCHAR(10) DEFAULT (LEFT('TYT',3)||LPAD(nextval('seq_type_travaux')::text, 5,'0')),
    nom VARCHAR,
    idunite VARCHAR,
    idtravaux VARCHAR,
    pu numeric,
    primary key(id),
    foreign key (idunite) references unite (id),
    foreign key (idtravaux) references travaux (id)
);

CREATE SEQUENCE seq_pu START WITH 1;
CREATE TABLE prixunitaire (
    id VARCHAR(10) DEFAULT (LEFT('PRU',3)||LPAD(nextval('seq_pu')::text, 5,'0')),
    idtypetravaux VARCHAR,
    pu numeric,
    date date,
    primary key(id),
    foreign key (idtypetravaux) references typeTravaux (id)
);

CREATE SEQUENCE seq_travaux_maison START WITH 1;
CREATE TABLE travauxmaison (
    id VARCHAR(10) DEFAULT (LEFT('TRM',3)||LPAD(nextval('seq_travaux_maison')::text, 5,'0')),
    idmaison VARCHAR,
    idtravaux VARCHAR,
    quantite numeric,
    primary key(id),
    foreign key (idmaison) references maison (id),
    foreign key (idtravaux) references typeTravaux (id)
);

CREATE SEQUENCE seq_travaux_devis START WITH 1;
CREATE TABLE travauxDevis(
    id VARCHAR(10) DEFAULT (LEFT('TRV',3)||LPAD(nextval('seq_travaux_devis')::text, 5,'0')),
    iddevis VARCHAR,
    idtypetravaux VARCHAR,
    quantite numeric,
    pu numeric,
    montant numeric,
    primary key(id),
    foreign key(idtypetravaux) references typeTravaux(id),
    foreign key (iddevis) references devis(id)
);

CREATE TABLE detail_devis (
    id serial primary key,
    iddevis VARCHAR,
    client VARCHAR,
    travaux VARCHAR,
    unite VARCHAR,
    quantite numeric,
    pu numeric,
    montant numeric,
    foreign key (iddevis) references devis(id)

);
ALTER TABLE detail_devis ADD COLUMN taux numeric;
ALTER TABLE detail_devis ADD COLUMN finition VARCHAR;
ALTER TABLE detail_devis ADD COLUMN code_travaux VARCHAR;

CREATE TABLE csv_travaux_maison (
    id serial primary key,
    type_maison VARCHAR,
    description VARCHAR,
    surface numeric,
    code_travaux VARCHAR,
    type_travaux VARCHAR,
    unite VARCHAR,
    prix_unitaire numeric,
    quantite numeric,
    duree_travaux int
);

CREATE TABLE csv_devis (
    id serial primary key,
    client VARCHAR,
    ref_devis VARCHAR,
    type_maison VARCHAR,
    finition VARCHAR,
    taux_finition numeric,
    date_devis date,
    date_debut date,
    lieu VARCHAR
);

CREATE TABLE csv_paiement (
    id serial primary key,
    ref_devis VARCHAR,
    ref_paiement VARCHAR,
    date_paiement date,
    montant numeric
);

CREATE TABLE erreur_data (
    id serial primary key,
    ligne int,
    erreur VARCHAR,
    nom_fichier VARCHAR
);

ALTER TABLE devis 
ALTER column prix SET DEFAULT calcul_prix(idfinition);

CREATE OR REPLACE view detail_typeTravaux AS
SELECT typeTravaux.id,typeTravaux.nom,unite.nom as unite,pu FROM
    typeTravaux JOIN unite ON unite.id = typeTravaux.idunite;

;

SELECT (sum(pu*quantite)+((sum(pu*quantite)*pourcentage)/100)) AS montant FROM finition,travauxmaison
    JOIN typetravaux ON typetravaux.id = travauxmaison.idtravaux
    WHERE finition.id = 'FIN00006'
    GROUP BY pourcentage;

CREATE OR REPLACE VIEW liste_devis AS
SELECT devis.id,idclient,maison.nom as maison,finition.nom as finition,devis.date,dateDebut,datefin,prix as montant ,COALESCE(SUM(montant),0) AS paye, COALESCE(prix-SUM(montant),prix) as reste FROM devis
    JOIN maison on maison.id = devis.idmaison
    JOIN finition on finition.id = devis.idfinition
    LEFT JOIN paiement ON devis.id = paiement.iddevis
    GROUP BY devis.id,idclient,maison,finition,prix,dateDebut,datefin;

CREATE OR REPLACE view histogramme AS 
SELECT EXTRACT(MONTH FROM months.mois_date) AS mois , TO_CHAR(months.mois_date,'Month') AS nom_mois, COALESCE(SUM(montant),0) as montants, EXTRACT(YEAR FROM months.mois_date) as annee
    FROM (SELECT generate_series('2024-01-01'::date,'2024-12-01'::date,'1 month') AS mois_date) AS months
    LEFT JOIN liste_devis ON EXTRACT(MONTH FROM liste_devis.date) = EXTRACT(MONTH FROM months.mois_date) 
    AND EXTRACT(YEAR FROM months.mois_date) = EXTRACT(YEAR FROM liste_devis.date )
    GROUP BY mois, nom_mois,annee
    ORDER BY annee,mois;

CREATE OR REPLACE VIEW montant_devis_mois AS
SELECT SUM(montant) as montant,  EXTRACT(MONTH FROM liste_devis.date) AS mois,
    EXTRACT(YEAR FROM liste_devis.date) AS annee FROM liste_devis
    GROUP BY
    EXTRACT (MONTH FROM date),
    EXTRACT (YEAR FROM date)
ORDER BY 
    EXTRACT (YEAR FROM date),
    EXTRACT (MONTH FROM date);

CREATE OR REPLACE VIEW montant_devis_annee AS
SELECT SUM(montant) as montant, EXTRACT(YEAR FROM liste_devis.date) AS annee 
    FROM liste_devis
    GROUP BY
    EXTRACT (YEAR FROM date)
ORDER BY 
    EXTRACT (YEAR FROM date);

UPDATE maison SET prix = (
    SELECT SUM(quantite*pu) FROM travauxmaison 
    JOIN typeTravaux ON travauxmaison.idtravaux = typeTravaux.id)
    WHERE id = (SELECT distinct(idmaison) FROM travauxmaison WHERE idtravaux = '102');

CREATE or REPLACE view detail_travaux_maison AS
SELECT travauxmaison.id,maison.id as idmaison,typeTravaux.id as idtravaux,typeTravaux.nom,quantite,pu,quantite*pu as montant FROM travauxmaison
    JOIN maison ON travauxmaison.idmaison = maison.id
    JOIN typeTravaux ON travauxmaison.idtravaux = typeTravaux.id;

CREATE or REPLACE view detail_travaux_devis AS
SELECT iddevis,idtypetravaux,typeTravaux.nom,quantite,travauxDevis.pu,montant,unite.nom as unite FROM travauxDevis
    JOIN typeTravaux ON travauxdevis.idtypetravaux = typeTravaux.id
    JOIN unite ON unite.id = typeTravaux.idunite;

CREATE OR REPLACE view detail_devis_1 AS
SELECT devis.id,client.username,typeTravaux.nom as travaux,unite.nom as unite,quantite,typeTravaux.pu,quantite*pu  as montant,finition.pourcentage as taux,finition.nom as finition,typeTravaux.id as code_travaux FROM devis 
    JOIN client ON devis.idclient = client.id 
    JOIN travauxmaison ON travauxmaison.idmaison = devis.idmaison 
    JOIN typeTravaux ON typeTravaux.id = travauxmaison.idtravaux
    JOIN unite ON typeTravaux.idunite = unite.id
    JOIN finition ON finition.id = devis.idfinition;
