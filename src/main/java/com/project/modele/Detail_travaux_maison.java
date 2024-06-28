package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.table.JDBC;
import java.sql.Connection;

@TableName(database = "btp", driver = "postgres", name = "detail_travaux_maison", password = "Maroussia1833", user = "postgres")
public class Detail_travaux_maison extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "idmaison")
	String idmaison;
	@ColumnField(columnName = "idtravaux")
	String idtravaux;
	@ColumnField(columnName = "nom")
	String nom;
	@ColumnField(columnName = "quantite")
	Double quantite;
	@ColumnField(columnName = "pu")
	Double pu;
	@ColumnField(columnName = "montant")
	Double montant;

	public Detail_travaux_maison() throws Exception {

	}

	public Detail_travaux_maison(String idmaison, String idtravaux, String nom, Double quantite, Double pu,
			Double montant) throws Exception {
		setIdmaison(idmaison.trim());
		setIdtravaux(idtravaux.trim());
		setNom(nom.trim());
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

	@Getter(columnName = "idmaison")
	public String getIdmaison() {
		return this.idmaison;
	}

	@Setter(columnName = "idmaison")
	public void setIdmaison(String idmaison) {
		this.idmaison = idmaison;
	}

	@Getter(columnName = "idtravaux")
	public String getIdtravaux() {
		return this.idtravaux;
	}

	@Setter(columnName = "idtravaux")
	public void setIdtravaux(String idtravaux) {
		this.idtravaux = idtravaux;
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

}
