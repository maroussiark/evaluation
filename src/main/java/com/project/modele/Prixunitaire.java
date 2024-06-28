package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.table.JDBC;
import java.sql.Connection;
import java.sql.Date;

@TableName(database = "btp", driver = "postgres", name = "prixunitaire", password = "Maroussia1833", user = "postgres")
public class Prixunitaire extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "idtypetravaux")
	String idtypetravaux;
	@ColumnField(columnName = "pu")
	Double pu;
	@ColumnField(columnName = "date")
	Date date;

	public Prixunitaire() throws Exception {

	}

	public Prixunitaire(String idtypetravaux, Double pu, Date date) throws Exception {
		setIdtypetravaux(idtypetravaux.trim());
		setPu(pu);
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

	@Getter(columnName = "idtypetravaux")
	public String getIdtypetravaux() {
		return this.idtypetravaux;
	}

	@Setter(columnName = "idtypetravaux")
	public void setIdtypetravaux(String idtypetravaux) {
		this.idtypetravaux = idtypetravaux;
	}

	@Getter(columnName = "pu")
	public Double getPu() {
		return this.pu;
	}

	@Setter(columnName = "pu")
	public void setPu(Double pu) {
		this.pu = pu;
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

}
