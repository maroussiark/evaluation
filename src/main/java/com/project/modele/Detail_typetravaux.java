package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.table.JDBC;
import com.project.tools.Formatter;

import java.sql.Connection;

@TableName(database = "btp", driver = "postgres", name = "detail_typetravaux", password = "Maroussia1833", user = "postgres")
public class Detail_typetravaux extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "nom")
	String nom;
	@ColumnField(columnName = "unite")
	String unite;
	@ColumnField(columnName = "pu")
	Double pu;

	public String getPuString() {
		return Formatter.formatThousand(pu);
	}

	public Detail_typetravaux() throws Exception {

	}

	public Detail_typetravaux(String nom, String unite, Double pu) throws Exception {
		setNom(nom.trim());
		setUnite(unite.trim());
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

	@Getter(columnName = "unite")
	public String getUnite() {
		return this.unite;
	}

	@Setter(columnName = "unite")
	public void setUnite(String unite) {
		this.unite = unite;
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
