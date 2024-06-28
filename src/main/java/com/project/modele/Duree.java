package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.table.JDBC;
import java.sql.Connection;

@TableName(database = "btp", driver = "postgres", name = "duree", password = "Maroussia1833", user = "postgres")
public class Duree extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "idmaison")
	String idmaison;
	@ColumnField(columnName = "valeur")
	Integer valeur;

	public Duree() throws Exception {

	}

	public Duree(String idmaison, Integer valeur) throws Exception {
		setIdmaison(idmaison.trim());
		setValeur(valeur);
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

	@Getter(columnName = "valeur")
	public Integer getValeur() {
		return this.valeur;
	}

	@Setter(columnName = "valeur")
	public void setValeur(Integer valeur) {
		this.valeur = valeur;
	}

}
