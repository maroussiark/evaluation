package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.table.JDBC;
import com.project.tools.Formatter;

import java.sql.Connection;
import java.util.LinkedList;

@TableName(database = "btp", driver = "postgres", name = "maison", password = "Maroussia1833", user = "postgres")
public class Maison extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "nom")
	String nom;
	LinkedList<Desc_maison> descriptions;

	public String getSurface() {
		Desc_maison desc = getDescription();
		return Formatter.formatThousand(desc.getSurface());
	}

	public LinkedList<String> descriptionList() {
		LinkedList<String> listdesc = new LinkedList<>();
		Desc_maison desc = getDescription();
		String[] lists = desc.getDescription().split(",");
		for (int i = 0; i < lists.length; i++) {
			listdesc.add(lists[i]);
		}
		return listdesc;
	}

	public Desc_maison getDescription() {
		Desc_maison desc_maison = null;
		try {
			desc_maison = (Desc_maison) new Desc_maison().select("WHERE idmaison='" + id + "'").getFirst();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return desc_maison;
	}

	public LinkedList<Desc_maison> getDescriptions() {
		return this.descriptions;
	}

	public void setDescriptions(LinkedList<Desc_maison> descriptions) {
		this.descriptions = descriptions;
	}

	public void setDescription_List(LinkedList<Maison> maisons, Connection connection) throws Exception {
		for (Maison maison : maisons) {
			LinkedList<Desc_maison> desc = new Desc_maison().select(connection, "WHERE idmaison='" + maison.id + "'");
			maison.setDescriptions(desc);
		}
	}

	public Maison() throws Exception {

	}

	public Maison(String nom) throws Exception {
		setNom(nom.trim());
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

}
