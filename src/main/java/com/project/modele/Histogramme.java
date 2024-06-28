package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.connection.Connector;
import com.project.table.JDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

@TableName(database = "btp", driver = "postgres", name = "histogramme", password = "Maroussia1833", user = "postgres")
public class Histogramme extends JDBC {

	@ColumnField(columnName = "mois")
	int mois;
	@ColumnField(columnName = "nom_mois")
	String nom_mois;
	@ColumnField(columnName = "montants")
	Double montants;
	@ColumnField(columnName = "annee")
	int annee;

	public LinkedList<String> getLabels(String annee, Connection connection) throws Exception {
		LinkedList<String> labels = new LinkedList<>();
		LinkedList<Histogramme> histogrammes = getHistogrammes(annee, connection);
		for (Histogramme histogramme : histogrammes) {
			labels.add(histogramme.getNom_mois());
		}
		return labels;
	}

	public LinkedList<Double> getDatas(String annee, Connection connection) throws Exception {
		LinkedList<Double> data = new LinkedList<>();
		LinkedList<Histogramme> histogrammes = getHistogrammes(annee, connection);
		for (Histogramme histogramme : histogrammes) {
			data.add(histogramme.getMontants());
		}
		return data;
	}

	public LinkedList<Histogramme> getHistogrammes(String annee, Connection connection) throws Exception {
		LinkedList<Histogramme> histogrammes = new LinkedList<Histogramme>();
		PreparedStatement statement = null;
		try {
			String date1 = annee + "-01-01";
			String date2 = annee + "-12-01";

			String sql = "SELECT EXTRACT(MONTH FROM months.mois_date) AS mois , TO_CHAR(months.mois_date,'Month') AS nom_mois, COALESCE(SUM(montant),0) as montants, EXTRACT(YEAR FROM months.mois_date) as annee"
					+
					" FROM (SELECT generate_series('" + date1
					+ "'::date,'" + date2 + "'::date,'1 month') AS mois_date) AS months "
					+
					" LEFT JOIN liste_devis ON EXTRACT(MONTH FROM liste_devis.date) = EXTRACT(MONTH FROM months.mois_date) "
					+
					" AND EXTRACT(YEAR FROM months.mois_date) = EXTRACT(YEAR FROM liste_devis.date ) " +
					" GROUP BY mois, nom_mois,annee " +
					" ORDER BY annee,mois ";
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Histogramme histogramme = new Histogramme();
				histogramme.setMois(resultSet.getInt("mois"));
				histogramme.setNom_mois(resultSet.getString("nom_mois"));
				histogramme.setMontants(resultSet.getDouble("montants"));
				histogramme.setAnnee(resultSet.getInt("annee"));
				histogrammes.add(histogramme);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Connector.CloseStatement(statement);
		}
		return histogrammes;
	}

	public Histogramme() throws Exception {

	}

	public Histogramme(int mois, String nom_mois, Double montants, int annee) throws Exception {
		setMois(mois);
		setNom_mois(nom_mois.trim());
		setMontants(montants);
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

	@Getter(columnName = "mois")
	public int getMois() {
		return this.mois;
	}

	@Setter(columnName = "mois")
	public void setMois(int mois) {
		this.mois = mois;
	}

	@Getter(columnName = "nom_mois")
	public String getNom_mois() {
		return this.nom_mois;
	}

	@Setter(columnName = "nom_mois")
	public void setNom_mois(String nom_mois) {
		this.nom_mois = nom_mois;
	}

	@Getter(columnName = "montants")
	public Double getMontants() {
		return this.montants;
	}

	@Setter(columnName = "montants")
	public void setMontants(Double montants) {
		this.montants = montants;
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
