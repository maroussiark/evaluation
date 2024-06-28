package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.connection.Connector;
import com.project.table.JDBC;
import com.project.tools.Formatter;

import java.sql.Connection;
import java.sql.Date;
import java.util.LinkedList;

@TableName(database = "btp", driver = "postgres", name = "liste_devis", password = "Maroussia1833", user = "postgres")
public class Liste_devis extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "idclient")
	String idclient;
	@ColumnField(columnName = "maison")
	String maison;
	@ColumnField(columnName = "finition")
	String finition;
	@ColumnField(columnName = "date")
	Date date;
	@ColumnField(columnName = "datedebut")
	Date datedebut;
	@ColumnField(columnName = "datefin")
	Date datefin;
	@ColumnField(columnName = "montant")
	Double montant;
	@ColumnField(columnName = "paye")
	Double paye;
	@ColumnField(columnName = "reste")
	Double reste;

	public String getCouleur() {
		String couleur = "";
		if (getPourcentage() < 50) {
			couleur = "table-danger";
		} else if (getPourcentage() > 50) {
			couleur = "table-success";
		} else {
			couleur = "";
		}
		return couleur;
	}

	public String getLieu() {
		String lieu = "";
		try {
			Devis devis = new Devis().selectById(id);
			lieu = devis.getLieu();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return lieu;
	}

	public double getSumTravaux() {
		double sum = 0;
		try {
			LinkedList<Detail_devis> detail_devis = new Detail_devis().select("WHERE iddevis='" + id + "'");
			for (Detail_devis detail_devis2 : detail_devis) {
				sum = sum + detail_devis2.getMontant();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return sum;
	}

	public String getVraiMontantString() {
		return Formatter.formatThousand(getVraiMontant());
	}

	public double getVraiMontant() {
		double response = 0;
		try {
			double sumTravaux = getSumTravaux();
			Detail_devis detail_devis = (Detail_devis) new Detail_devis().select("WHERE iddevis='" + id + "'")
					.getFirst();
			double taux = detail_devis.getTaux();
			double tauxparSum = (taux * getSumTravaux()) / 100;
			response = sumTravaux + tauxparSum;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return response;
	}

	public String getSumPayeString() {
		Connection connection = null;
		String response = "";
		try {
			connection = Connector.connect();
			response = Formatter.formatThousand(getTotalPaiement(connection));
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Connector.CloseConnection(connection);
		}
		return response;
	}

	public String getSumTotalString() {
		Connection connection = null;
		String response = "";
		try {
			connection = Connector.connect();
			response = Formatter.formatThousand(getTotalMontant(connection));
			connection.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Connector.CloseConnection(connection);
		}
		return response;
	}

	public double getPourcentage() {
		double pour = 0;
		Connection connection = null;
		try {
			connection = Connector.connect();
			double montant = getTotalMontant(connection);
			if (montant == 0) {
				return 0;
			}
			pour = (getPaye() * 100) / montant;

			connection.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Connector.CloseConnection(connection);

		}
		return pour;
	}

	public double getTotalPaiement(Connection connection) {
		double sum = 0;
		try {
			LinkedList<Liste_devis> liste_devis = select(connection);
			for (Liste_devis liste_devis2 : liste_devis) {
				sum = sum + liste_devis2.getPaye();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return sum;
	}

	public double getTotalMontant(Connection connection) {
		double sum = 0;
		try {
			LinkedList<Liste_devis> liste_devis = select(connection);
			for (Liste_devis liste_devis2 : liste_devis) {
				sum = sum + liste_devis2.getVraiMontant();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return sum;
	}

	public String getPayeString() {
		return Formatter.formatThousand(paye);
	}

	public String getResteString() {
		return Formatter.formatThousand(reste);
	}

	public String getMontantString() {
		return Formatter.formatThousand(montant);
	}

	public Liste_devis() throws Exception {

	}

	public Liste_devis(String idclient, String maison, String finition, Date date, Date datedebut, Date datefin,
			Double montant, Double paye, Double reste) throws Exception {
		setIdclient(idclient.trim());
		setMaison(maison.trim());
		setFinition(finition.trim());
		setDate(date);
		setDatedebut(datedebut);
		setDatefin(datefin);
		setMontant(montant);
		setPaye(paye);
		setReste(reste);
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

	@Getter(columnName = "id")
	public String getId() {
		return this.id;
	}

	@Setter(columnName = "id")
	public void setId(String id) {
		this.id = id;
	}

	@Getter(columnName = "idclient")
	public String getIdclient() {
		return this.idclient;
	}

	@Setter(columnName = "idclient")
	public void setIdclient(String idclient) {
		this.idclient = idclient;
	}

	@Getter(columnName = "maison")
	public String getMaison() {
		return this.maison;
	}

	@Setter(columnName = "maison")
	public void setMaison(String maison) {
		this.maison = maison;
	}

	@Getter(columnName = "finition")
	public String getFinition() {
		return this.finition;
	}

	@Setter(columnName = "finition")
	public void setFinition(String finition) {
		this.finition = finition;
	}

	@Getter(columnName = "date")
	public Date getDate() {
		return this.date;
	}

	@Setter(columnName = "date")
	public void setDate(Date date) {
		this.date = date;
	}

	public void setDate(String date) {
		Date daty_temp = Date.valueOf(date.trim());
		setDate(daty_temp);
	}

	@Getter(columnName = "datedebut")
	public Date getDatedebut() {
		return this.datedebut;
	}

	@Setter(columnName = "datedebut")
	public void setDatedebut(Date datedebut) {
		this.datedebut = datedebut;
	}

	public void setDatedebut(String datedebut) {
		Date daty_temp = Date.valueOf(datedebut.trim());
		setDatedebut(daty_temp);
	}

	@Getter(columnName = "datefin")
	public Date getDatefin() {
		return this.datefin;
	}

	@Setter(columnName = "datefin")
	public void setDatefin(Date datefin) {
		this.datefin = datefin;
	}

	public void setDatefin(String datefin) {
		Date daty_temp = Date.valueOf(datefin.trim());
		setDatefin(daty_temp);
	}

	@Getter(columnName = "montant")
	public Double getMontant() {
		return this.montant;
	}

	@Setter(columnName = "montant")
	public void setMontant(Double montant) {
		this.montant = montant;
	}

	@Getter(columnName = "paye")
	public Double getPaye() {
		return this.paye;
	}

	@Setter(columnName = "paye")
	public void setPaye(Double paye) {
		this.paye = paye;
	}

	@Getter(columnName = "reste")
	public Double getReste() {
		return this.reste;
	}

	@Setter(columnName = "reste")
	public void setReste(Double reste) {
		this.reste = reste;
	}

}
