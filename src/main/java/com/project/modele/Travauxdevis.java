package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.table.JDBC;
import java.sql.Connection;

@TableName(database = "btp", driver = "postgres", name = "travauxdevis", password = "Maroussia1833", user = "postgres")
public class Travauxdevis extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "iddevis")
	String iddevis;
	@ColumnField(columnName = "idtypetravaux")
	String idtypetravaux;
	@ColumnField(columnName = "quantite")
	Double quantite;
	@ColumnField(columnName = "pu")
	Double pu;
	@ColumnField(columnName = "montant")
	Double montant;

	public Travauxdevis() throws Exception {

	}

	public Travauxdevis(String iddevis, String idtypetravaux, Double quantite, Double pu, Double montant)
			throws Exception {
		setIddevis(iddevis.trim());
		setIdtypetravaux(idtypetravaux.trim());
		setQuantite(quantite);
		setPu(pu);
		setMontant(montant);
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

	@Getter(columnName = "idtypetravaux")
	public String getIdtypetravaux() {
		return this.idtypetravaux;
	}

	@Setter(columnName = "idtypetravaux")
	public void setIdtypetravaux(String idtypetravaux) {
		this.idtypetravaux = idtypetravaux;
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

}
