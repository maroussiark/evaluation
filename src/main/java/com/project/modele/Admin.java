package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.config.User;
import com.project.table.JDBC;
import java.sql.Connection;

@TableName(database = "btp", driver = "postgres", name = "admin", password = "Maroussia1833", user = "postgres")
public class Admin extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	Long id;
	@ColumnField(columnName = "username")
	String username;
	@ColumnField(columnName = "password")
	String password;
	@ColumnField(columnName = "profil")
	String profil;

	public User log(Connection connection) throws Exception {
		User user = null;
		if (!select(connection, "WHERE username='" + username + "' AND password='" + password + "'").isEmpty()) {
			Admin admin = (Admin) select(connection,
					"WHERE username='" + username + "' AND password='" + password + "'")
					.getFirst();
			user = new User(admin.getId().toString(), admin.getUsername(), admin.getProfil());
		} else {
			throw new Exception("Erreur d'authentification");
		}

		return user;
	}

	public Admin() throws Exception {

	}

	public Admin(String username, String password, String profil) throws Exception {
		setUsername(username.trim());
		setPassword(password.trim());
		setProfil(profil.trim());
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
	public Long getId() {
		return this.id;
	}

	@Setter(columnName = "id")
	public void setId(Long id) {
		this.id = id;
	}

	@Getter(columnName = "username")
	public String getUsername() {
		return this.username;
	}

	@Setter(columnName = "username")
	public void setUsername(String username) {
		this.username = username;
	}

	@Getter(columnName = "password")
	public String getPassword() {
		return this.password;
	}

	@Setter(columnName = "password")
	public void setPassword(String password) {
		this.password = password;
	}

	@Getter(columnName = "profil")
	public String getProfil() {
		return this.profil;
	}

	@Setter(columnName = "profil")
	public void setProfil(String profil) {
		this.profil = profil;
	}

}
