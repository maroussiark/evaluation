package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.table.JDBC;
import com.project.tools.Formatter;

import java.sql.Connection;
import java.sql.Date;
import java.util.LinkedList;

@TableName(database = "btp", driver = "postgres", name = "paiement", password = "Maroussia1833", user = "postgres")
public class Paiement extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "iddevis")
	String iddevis;
	@ColumnField(columnName = "montant")
	Double montant;
	@ColumnField(columnName = "date")
	Date date;
	@ColumnField(columnName = "ref")
	String ref;

	public double getSum(LinkedList<Paiement> paiements) {
		double sum = 0;
		for (Paiement paiement : paiements) {
			sum = sum + paiement.getMontant();
		}
		return sum;
	}

	public void payer(Connection connection) throws Exception {
		if (montant <= 0) {
			throw new Exception("Veuillez verifier la valeur du montant");
		}
		if (iddevis == null || montant == null) {
			throw new Exception("Veuillez bien remplir les informations");
		}
		if (montant > getDevis(connection).getRestePaye()) {
			throw new Exception("Montant total des paiements depasser");
		}
		insert(connection);
	}

	public Devis getDevis(Connection connection) throws Exception {
		Devis devis = (Devis) new Devis().select(connection, " WHERE id='" + iddevis + "'").getFirst();
		return devis;
	}

	public String getMontantString() {
		return Formatter.formatThousand(montant);
	}

	public Paiement() throws Exception {

	}

	public Paiement(String iddevis, Double montant, String date) throws Exception {
		setIddevis(iddevis.trim());
		setMontant(montant);
		setDate(date);
	}

	public Paiement(String iddevis, Double montant, Date date) throws Exception {
		setIddevis(iddevis.trim());
		setMontant(montant);
		setDate(date);
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

	@Getter(columnName = "iddevis")
	public String getIddevis() {
		return this.iddevis;
	}

	@Setter(columnName = "iddevis")
	public void setIddevis(String iddevis) {
		this.iddevis = iddevis;
	}

	@Getter(columnName = "montant")
	public Double getMontant() {
		return this.montant;
	}

	@Setter(columnName = "montant")
	public void setMontant(Double montant) {
		this.montant = montant;
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

	@Getter(columnName = "ref")
	public String getRef() {
		return this.ref;
	}

	@Setter(columnName = "ref")
	public void setRef(String ref) {
		this.ref = ref;
	}
}
