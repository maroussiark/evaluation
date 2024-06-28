package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.table.JDBC;
import java.sql.Connection;

@TableName(database = "btp", driver = "postgres", name = "finition", password = "Maroussia1833", user = "postgres")
public class Finition extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "nom")
	String nom;
	@ColumnField(columnName = "pourcentage")
	Double pourcentage;

	public void updateFinition(Connection connection) throws Exception {
		if (pourcentage == null) {
			throw new Exception("Veuillez bien remplir les informations");
		}
		update(connection, "pourcentage", pourcentage.toString());
	}

	public Finition() throws Exception {

	}

	public Finition(Double pourcentage) throws Exception {
		setPourcentage(pourcentage);
	}

	public Finition(String nom, Double pourcentage) throws Exception {
		setNom(nom.trim());
		setPourcentage(pourcentage);
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

	@Getter(columnName = "pourcentage")
	public Double getPourcentage() {
		return this.pourcentage;
	}

	@Setter(columnName = "pourcentage")
	public void setPourcentage(Double pourcentage) {
		this.pourcentage = pourcentage;
	}

}
