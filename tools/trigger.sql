CREATE OR REPLACE FUNCTION maj_prix_maison()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE maison
        SET prix = prix + (NEW.quantite * (SELECT pu FROM typeTravaux WHERE id = NEW.idtravaux))
        WHERE id = NEW.idmaison;
    ELSIF TG_OP = 'UPDATE' THEN
        IF TG_TABLE_NAME = 'typeTravaux' THEN
                UPDATE maison
                SET prix = prix + ((SELECT quantite FROM travauxmaison WHERE idtravaux = NEW.id )  * NEW.pu)
                WHERE id = (SELECT idmaison FROM travauxmaison WHERE idtravaux = NEW.id);
        ELSEIF  TG_TABLE_NAME = 'travauxmaison' THEN
            IF NEW.quantite <> OLD.quantite THEN
                UPDATE maison
                SET prix = prix + ((NEW.quantite - OLD.quantite) * (SELECT pu FROM typetravaux WHERE id = NEW.idtravaux))
                WHERE id = NEW.idmaison;
            END IF;
        END IF;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER maj_prix_maison_trigger
AFTER INSERT OR UPDATE ON travauxmaison
FOR EACH ROW
EXECUTE FUNCTION maj_prix_maison();

CREATE TRIGGER maj_prix_maison_travaux_trigger
AFTER UPDATE ON typetravaux
FOR EACH ROW
EXECUTE FUNCTION maj_prix_maison();

DROP TRIGGER IF EXISTS calcul_devis_trigger ON devis;

CREATE OR REPLACE FUNCTION calcul_devis()
RETURNS TRIGGER AS $$
BEGIN 
    NEW.datefin := NEW.datedebut + (SELECT valeur FROM duree WHERE idmaison = NEW.idmaison);
    NEW.prix := (SELECT (maison.prix + (maison.prix*pourcentage)/100) AS montant FROM maison,finition
    WHERE maison.id::text=NEW.idmaison::text AND finition.id::text=NEW.idfinition::text);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER calcul_devis_trigger
BEFORE INSERT ON devis
FOR EACH ROW
EXECUTE FUNCTION calcul_devis();


SELECT (maison.prix + (maison.prix*pourcentage)/100) AS montant FROM maison
    JOIN devis ON devis.idmaison = maison.id
    JOIN finition ON finition.id = devis.idfinition;


SELECT (maison.prix + (maison.prix*pourcentage)/100) AS montant FROM maison,finition
    WHERE maison.id='MAI00023' AND finition.id='FIN00013';