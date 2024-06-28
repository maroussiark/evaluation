package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.table.JDBC;
import java.sql.Connection;

@TableName(database = "btp", driver = "postgres", name = "desc_maison", password = "Maroussia1833", user = "postgres")
public class Desc_maison extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "idmaison")
	String idmaison;
	@ColumnField(columnName = "description")
	String description;
	@ColumnField(columnName = "surface")
	Double surface;

	public Desc_maison() throws Exception {

	}

	public Desc_maison(String idmaison, String description, Double surface) throws Exception {
		setIdmaison(idmaison.trim());
		setDescription(description.trim());
		setSurface(surface);
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

	@Getter(columnName = "description")
	public String getDescription() {
		return this.description;
	}

	@Setter(columnName = "description")
	public void setDescription(String description) {
		this.description = description;
	}

	@Getter(columnName = "surface")
	public Double getSurface() {
		return this.surface;
	}

	@Setter(columnName = "surface")
	public void setSurface(Double surface) {
		this.surface = surface;
	}

}
