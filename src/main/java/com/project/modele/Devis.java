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
import java.sql.PreparedStatement;
import java.util.LinkedList;

@TableName(database = "btp", driver = "postgres", name = "devis", password = "Maroussia1833", user = "postgres")
public class Devis extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "idclient")
	String idclient;
	@ColumnField(columnName = "idmaison")
	String idmaison;
	@ColumnField(columnName = "idfinition")
	String idfinition;
	@ColumnField(columnName = "date")
	Date date;
	@ColumnField(columnName = "datedebut")
	Date datedebut;
	@ColumnField(columnName = "prix")
	Double prix;
	@ColumnField(columnName = "datefin")
	Date datefin;
	@ColumnField(columnName = "lieu")
	String lieu;

	public String getMontantString() {
		return Formatter.formatThousand(getVraiMontant());
	}

	public double getVraiMontant() {
		double reponse = 0;
		Liste_devis liste_devis = get_detail_devis();
		reponse = liste_devis.getVraiMontant();
		return reponse;
	}

	public Liste_devis get_detail_devis() {
		Connection connection = null;
		Liste_devis liste_devis = null;

		try {
			connection = Connector.connect();
			liste_devis = (Liste_devis) new Liste_devis().select(connection, "WHERE id='" + id + "'")
					.getFirst();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			Connector.CloseConnection(connection);
		}
		return liste_devis;
	}

	public double getPaye() {
		double paye = 0;
		paye = get_detail_devis().getPaye();
		return paye;
	}

	public double getRestePaye() {
		double reste = 0;
		reste = get_detail_devis().getReste();
		return reste;
	}

	public String getSommeTravauxString() {
		String sum = "";
		Connection connection = null;
		try {
			connection = Connector.connect();
			sum = Formatter.formatThousand(getSommeTravaux(connection));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			Connector.CloseConnection(connection);
		}
		return sum;
	}

	public double getSommeTravaux(Connection connection) throws Exception {
		double sum = 0;
		LinkedList<Detail_devis> detail_devis = new Detail_devis().select(connection, "WHERE iddevis='" + id + "'");
		for (Detail_devis detail_devis2 : detail_devis) {
			sum = detail_devis2.getMontant() + sum;
		}

		return sum;
	}

	public String getPrixString() {
		return Formatter.formatThousand(prix);
	}

	public LinkedList<Detail_devis> detail_devis() throws Exception {
		LinkedList<Detail_devis> detail_devis = new LinkedList<>();
		Connection connection = null;
		try {
			connection = Connector.connect();
			detail_devis = new Detail_devis().select(connection, "WHERE iddevis = '" + id + "'");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			Connector.CloseConnection(connection);
		}

		return detail_devis;
	}

	public LinkedList<Detail_travaux_maison> getTravauxMaison(Connection connection) throws Exception {
		return new Detail_travaux_maison()
				.select("WHERE idmaison='" + idmaison + "'");
	}

	public Finition getFinition(Connection connection) throws Exception {
		return (Finition) new Finition().select("WHERE id='" + idfinition + "'").getFirst();
	}

	public Duree getDuree(Connection connection) throws Exception {

		if (new Duree().select(connection, "WHERE idmaison='" + idmaison + "'").isEmpty()) {
			throw new Exception("Choisissez une maison");
		}
		return (Duree) new Duree().select(connection, "WHERE idmaison='" + idmaison + "'").getFirst();
	}

	public double calculPrix(Connection connection) throws Exception {
		double montant = 0;
		LinkedList<Detail_travaux_maison> travaux_maisons = getTravauxMaison(connection);
		double totalPrix = 0;
		for (Detail_travaux_maison detail_travaux_maison : travaux_maisons) {
			totalPrix = totalPrix + detail_travaux_maison.getMontant();
		}
		Finition finition = getFinition(connection);
		double pourcentage = (totalPrix * finition.getPourcentage()) / 100;
		montant = totalPrix + pourcentage;
		return montant;
	}

	public Devis(String idclient, String idmaison, String idfinition) throws Exception {
		setIdclient(idclient);
		setIdmaison(idmaison);
		setIdfinition(idfinition);
	}

	@SuppressWarnings("removal")
	public String create(String daty, Connection connection) throws Exception {
		String iddevis = "";
		if (idclient == null || idfinition == null || idmaison == null || daty == null) {
			throw new Exception("Veuillez bien remplir les informations");
		}
		setPrix(new Double("0"));
		// String datee = Formatter.formatDate(daty, "dd/mm/aaaa", "aaaa-mm-dd");
		Date daty_temp = Date.valueOf(daty.trim());
		if (daty_temp.before(new java.util.Date(System.currentTimeMillis()))) {
			throw new Exception("La date debut doit etre superieur a ajourd'hui");
		}

		Duree duree = getDuree(connection);
		Date dateFin = Formatter.addDays(daty_temp, duree.getValeur());
		double prix = calculPrix(connection);

		setDatedebut(daty_temp);
		setDate(new Date(System.currentTimeMillis()));
		setDatefin(dateFin);
		setPrix(prix);

		iddevis = insertAndReturnId(connection);

		// LinkedList<Detail_travaux_maison> travaux_maisons =
		// getTravauxMaison(connection);
		// for (Detail_travaux_maison travaux : travaux_maisons) {
		// Travauxdevis tr = new Travauxdevis(iddevis, travaux.getIdtravaux(),
		// travaux.getQuantite(),
		// travaux.getPu(), travaux.getMontant());
		// tr.insert(connection);
		// }

		String sql = "INSERT INTO detail_devis(iddevis,client,travaux,unite,quantite,pu,montant,taux,finition,code_travaux) SELECT * FROM detail_devis_1 WHERE id='"
				+ iddevis + "'";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();

		return iddevis;
	}

	public Devis() throws Exception {

	}

	public Devis(String idclient, String idmaison, String idfinition, Date date, Date datedebut, Double prix)
			throws Exception {
		setIdclient(idclient.trim());
		setIdmaison(idmaison.trim());
		setIdfinition(idfinition.trim());
		setDate(date);
		setDatedebut(datedebut);
		setPrix(prix);
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

	@Getter(columnName = "idmaison")
	public String getIdmaison() {
		return this.idmaison;
	}

	@Setter(columnName = "idmaison")
	public void setIdmaison(String idmaison) {
		this.idmaison = idmaison;
	}

	@Getter(columnName = "idfinition")
	public String getIdfinition() {
		return this.idfinition;
	}

	@Setter(columnName = "idfinition")
	public void setIdfinition(String idfinition) {
		this.idfinition = idfinition;
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

	@Getter(columnName = "prix")
	public Double getPrix() {
		return this.prix;
	}

	@Setter(columnName = "prix")
	public void setPrix(Double prix) {
		this.prix = prix;
	}

	@Getter(columnName = "lieu")
	public String getLieu() {
		return this.lieu;
	}

	@Setter(columnName = "lieu")
	public void setLieu(String lieu) {
		this.lieu = lieu;
	}
}
