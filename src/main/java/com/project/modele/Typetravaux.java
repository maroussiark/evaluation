package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.table.JDBC;
import java.sql.Connection;
import java.util.LinkedList;

@TableName(database = "btp", driver = "postgres", name = "typetravaux", password = "Maroussia1833", user = "postgres")
public class Typetravaux extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "nom")
	String nom;
	@ColumnField(columnName = "idunite")
	String idunite;
	@ColumnField(columnName = "idtravaux")
	String idtravaux;
	@ColumnField(columnName = "pu")
	Double pu;

	// public LinkedList<String> getMaisonsLie(Connection connection) {
	// // LinkedList<String>
	// }

	public void updateTravaux(Connection connection) throws Exception {
		if (nom == null || idunite == null) {
			throw new Exception("Veuillez bien remplir les informations");
		}
		if (pu <= 0) {
			throw new Exception("Verifier la valeur du prix unitaire");
		}
		System.out.println(id);
		update(connection, "nom", nom);
		update(connection, "idunite", idunite);
		update(connection, "pu", pu.toString());
	}

	public Typetravaux() throws Exception {

	}

	public Typetravaux(String nom, String idunite, Double pu) throws Exception {
		setNom(nom.trim());
		setIdunite(idunite.trim());
		setPu(pu);
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

	@Getter(columnName = "nom")
	public String getNom() {
		return this.nom;
	}

	@Setter(columnName = "nom")
	public void setNom(String nom) {
		this.nom = nom;
	}

	@Getter(columnName = "idunite")
	public String getIdunite() {
		return this.idunite;
	}

	@Setter(columnName = "idunite")
	public void setIdunite(String idunite) {
		this.idunite = idunite;
	}

	@Getter(columnName = "idtravaux")
	public String getIdtravaux() {
		return this.idtravaux;
	}

	@Setter(columnName = "idtravaux")
	public void setIdtravaux(String idtravaux) {
		this.idtravaux = idtravaux;
	}

	@Getter(columnName = "pu")
	public Double getPu() {
		return this.pu;
	}

	@Setter(columnName = "pu")
	public void setPu(Double pu) {
		this.pu = pu;
	}

}
