package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.table.JDBC;
import com.project.tools.Formatter;

import java.sql.Connection;

@TableName(database = "btp", driver = "postgres", name = "detail_travaux_devis", password = "Maroussia1833", user = "postgres")
public class Detail_travaux_devis extends JDBC {

	@ColumnField(columnName = "iddevis")
	String iddevis;
	@ColumnField(columnName = "idtypetravaux")
	String idtypetravaux;
	@ColumnField(columnName = "nom")
	String nom;
	@ColumnField(columnName = "quantite")
	Double quantite;
	@ColumnField(columnName = "pu")
	Double pu;
	@ColumnField(columnName = "montant")
	Double montant;
	@ColumnField(columnName = "unite")
	String unite;

	public String getPuString() {
		return Formatter.formatThousand(pu);
	}

	public String getMontantString() {
		return Formatter.formatThousand(montant);
	}

	public Detail_travaux_devis() throws Exception {

	}

	public Detail_travaux_devis(String iddevis, String idtypetravaux, String nom, Double quantite, Double pu,
			Double montant, String unite) throws Exception {
		setIddevis(iddevis.trim());
		setIdtypetravaux(idtypetravaux.trim());
		setNom(nom.trim());
		setQuantite(quantite);
		setPu(pu);
		setMontant(montant);
		setUnite(unite.trim());
	}

	public int count(Connection connection) throws Exception {
		int count = 0;
		try {
			count = select(connection).size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Getter(columnName = "iddevis")
	public String getIddevis() {
		return this.iddevis;
	}

	@Setter(columnName = "iddevis")
	public void setIddevis(String iddevis) {
		this.iddevis = iddevis;
	}

	@Getter(columnName = "idtypetravaux")
	public String getIdtypetravaux() {
		return this.idtypetravaux;
	}

	@Setter(columnName = "idtypetravaux")
	public void setIdtypetravaux(String idtypetravaux) {
		this.idtypetravaux = idtypetravaux;
	}

	@Getter(columnName = "nom")
	public String getNom() {
		return this.nom;
	}

	@Setter(columnName = "nom")
	public void setNom(String nom) {
		this.nom = nom;
	}

	@Getter(columnName = "quantite")
	public Double getQuantite() {
		return this.quantite;
	}

	@Setter(columnName = "quantite")
	public void setQuantite(Double quantite) {
		this.quantite = quantite;
	}

	@Getter(columnName = "pu")
	public Double getPu() {
		return this.pu;
	}

	@Setter(columnName = "pu")
	public void setPu(Double pu) {
		this.pu = pu;
	}

	@Getter(columnName = "montant")
	public Double getMontant() {
		return this.montant;
	}

	@Setter(columnName = "montant")
	public void setMontant(Double montant) {
		this.montant = montant;
	}

	@Getter(columnName = "unite")
	public String getUnite() {
		return this.unite;
	}

	@Setter(columnName = "unite")
	public void setUnite(String unite) {
		this.unite = unite;
	}

}
