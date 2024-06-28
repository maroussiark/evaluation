INSERT INTO admin VALUES (DEFAULT, 'admin','admin','admin');

INSERT INTO maison VALUES(DEFAULT,'Maison 1');
INSERT INTO maison VALUES(DEFAULT,'Maison 2');
INSERT INTO maison VALUES(DEFAULT,'Maison 3');

INSERT INTO desc_maison VALUES (DEFAULT,'MAI00001','3 chambres');
INSERT INTO desc_maison VALUES (DEFAULT,'MAI00001','1 cuisine');
INSERT INTO desc_maison VALUES (DEFAULT,'MAI00001','1 living');
INSERT INTO desc_maison VALUES (DEFAULT,'MAI00001','2 toilettes');


INSERT INTO desc_maison VALUES (DEFAULT,'MAI00002','5 chambres');
INSERT INTO desc_maison VALUES (DEFAULT,'MAI00002','2 cuisine');
INSERT INTO desc_maison VALUES (DEFAULT,'MAI00002','1 living');
INSERT INTO desc_maison VALUES (DEFAULT,'MAI00002','3 toilettes');


INSERT INTO desc_maison VALUES (DEFAULT,'MAI00003','2 chambres');
INSERT INTO desc_maison VALUES (DEFAULT,'MAI00003','1 cuisine');
INSERT INTO desc_maison VALUES (DEFAULT,'MAI00003','1 living');
INSERT INTO desc_maison VALUES (DEFAULT,'MAI00003','1 toilettes');

INSERT INTO finition VALUES (DEFAULT,'Standard',0);
INSERT INTO finition VALUES (DEFAULT,'Gold',20);
INSERT INTO finition VALUES (DEFAULT,'Premium',30);
INSERT INTO finition VALUES (DEFAULT,'VIP',50);

INSERT INTO duree VALUES (DEFAULT,'MAI00001',120);
INSERT INTO duree VALUES (DEFAULT,'MAI00002',240);
INSERT INTO duree VALUES (DEFAULT,'MAI00003',60);

INSERT INTO travaux VALUES (DEFAULT,'Travaux preparatoire');
INSERT INTO travaux VALUES (DEFAULT,'Travaux de terrassement');
INSERT INTO travaux VALUES (DEFAULT,'Travaux d''infrastructure');

INSERT INTO unite VALUES (DEFAULT,'m3',0);
INSERT INTO unite VALUES (DEFAULT,'m2',0);
INSERT INTO unite VALUES (DEFAULT,'fft',0);

INSERT INTO typeTravaux VALUES (DEFAULT,'mur de soutemement','UNI00001','TRA00001',190000);
INSERT INTO typeTravaux VALUES (DEFAULT,'decapage des terrains meubles','UNI00002','TRA00002',3072.87);
INSERT INTO typeTravaux VALUES (DEFAULT,'dressage du plateforme','UNI00002','TRA00002',3736.26);
INSERT INTO typeTravaux VALUES (DEFAULT,'Fouille d''ouvrage terrain ferme','UNI00001','TRA00002',9390.93);
INSERT INTO typeTravaux VALUES (DEFAULT,'Rembai d''ouvrage','UNI00001','TRA00002',37563.26);
INSERT INTO typeTravaux VALUES (DEFAULT,'Travaux d''implantation','UNI00003','TRA00002',152656);
INSERT INTO typeTravaux VALUES (DEFAULT,'maconnerie de moellon','UNI00001','TRA00003',172114.40);
INSERT INTO typeTravaux VALUES (DEFAULT,'betion armee semelle isolee','UNI00001','TRA00003',573215.80);
INSERT INTO typeTravaux VALUES (DEFAULT,'betion armee amorces poteaux','UNI00001','TRA00003',573215.80);
INSERT INTO typeTravaux VALUES (DEFAULT,'betion armee chainage bas','UNI00001','TRA00003',573215.80);
INSERT INTO typeTravaux VALUES (DEFAULT,'remblai technique','UNI00001','TRA00003',37563.26);
INSERT INTO typeTravaux VALUES (DEFAULT,'herissonage','UNI00001','TRA00003',73245.40);
INSERT INTO typeTravaux VALUES (DEFAULT,'beton ordinaire','UNI00001','TRA00003',487815.80);
INSERT INTO typeTravaux VALUES (DEFAULT,'chape de 2cm','UNI00001','TRA00003',33566.54);

INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00001',26.98);
INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00002',101.36);
INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00003',101.36);
INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00004',24.44);
INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00005',15.59);
INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00006',1.00);
INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00007',9.62);
INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00008',0.53);
INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00009',0.56);
INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00010',2.44);
INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00011',15.59);
INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00012',7.80);
INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00013',5.46);
INSERT INTO travauxmaison VALUES (DEFAULT,'MAI00001','TYT00013',77.97);

INSERT INTO travauxDevis(idtypetravaux,quantite,pu,montant) SELECT idtravaux,quantite,pu,montant FROM detail_travaux_maison WHERE idmaison = 'MAI00001';
---IMPORT TRAVAUX MAISON --------
INSERT INTO maison(nom) SELECT distinct(type_maison) FROM csv_travaux_maison;
INSERT INTO desc_maison (idmaison,description,surface) SELECT distinct(maison.id),description,surface FROM csv_travaux_maison JOIN maison on csv_travaux_maison.type_maison = maison.nom;
INSERT INTO unite(nom) SELECT distinct(unite) FROM csv_travaux_maison;
INSERT INTO typeTravaux (id,nom,idunite,pu) SELECT distinct(code_travaux),type_travaux,unite.id,prix_unitaire FROM csv_travaux_maison JOIN unite on unite.nom = csv_travaux_maison.unite;
INSERT INTO duree(idmaison,valeur) SELECT distinct(maison.id),duree_travaux FROM csv_travaux_maison JOIN maison ON maison.nom = csv_travaux_maison.type_maison;
INSERT INTO travauxmaison(idmaison,idtravaux,quantite) SELECT maison.id,typeTravaux.id,quantite FROM csv_travaux_maison JOIN maison on maison.nom = csv_travaux_maison.type_maison JOIN typeTravaux on typeTravaux.nom = csv_travaux_maison.type_travaux;

---IMPORT DEVIS --------
INSERT INTO client(username) SELECT distinct(client) FROM csv_devis;
INSERT INTO finition(nom,pourcentage) SELECT distinct(finition),taux_finition FROM csv_devis;
INSERT INTO devis(id,idclient,idmaison,idfinition,date,datedebut,lieu) SELECT ref_devis,client.id,maison.id,finition.id,date_devis,date_debut,lieu FROM csv_devis JOIN client on client.username = csv_devis.client JOIN maison ON maison.nom = csv_devis.type_maison JOIN finition ON finition.nom = csv_devis.finition;
INSERT INTO detail_devis(iddevis,client,travaux,unite,quantite,pu,montant,taux,finition,code_travaux) SELECT * FROM detail_devis_1;

INSERT INTO paiement SELECT (ref_paiement,ref_devis,montant,date) FROM csv_paiement;

INSERT INTO csv_travaux_maison VALUES (DEFAULT,'TOKYO','test',128,'102','test','m2',100,100,100);
INSERT INTO csv_travaux_maison VALUES (DEFAULT,'TOKYO2','test',128,'102','test','m2',100,101,101);