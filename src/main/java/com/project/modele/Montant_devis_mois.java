package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.table.JDBC;
import java.sql.Connection;

@TableName(database = "btp", driver = "postgres", name = "montant_devis_mois", password = "Maroussia1833", user = "postgres")
public class Montant_devis_mois extends JDBC {

	@ColumnField(columnName = "montant")
	Double montant;
	@ColumnField(columnName = "mois")
	int mois;
	@ColumnField(columnName = "annee")
	int annee;

	public Montant_devis_mois() throws Exception {

	}

	public Montant_devis_mois(Double montant, int mois, int annee) throws Exception {
		setMontant(montant);
		setMois(mois);
		setAnnee(annee);
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

	@Getter(columnName = "montant")
	public Double getMontant() {
		return this.montant;
	}

	@Setter(columnName = "montant")
	public void setMontant(Double montant) {
		this.montant = montant;
	}

	@Getter(columnName = "mois")
	public int getMois() {
		return this.mois;
	}

	@Setter(columnName = "mois")
	public void setMois(int mois) {
		this.mois = mois;
	}

	@Getter(columnName = "annee")
	public int getAnnee() {
		return this.annee;
	}

	@Setter(columnName = "annee")
	public void setAnnee(int annee) {
		this.annee = annee;
	}

}
