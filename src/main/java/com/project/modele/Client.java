package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.config.User;
import com.project.connection.Connector;
import com.project.table.JDBC;
import java.sql.Connection;

@TableName(database = "btp", driver = "postgres", name = "client", password = "Maroussia1833", user = "postgres")
public class Client extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	String id;
	@ColumnField(columnName = "username")
	String username;
	@ColumnField(columnName = "profil")
	String profil;

	public Client(String id, String username, String profil) throws Exception {
		this.id = id;
		this.username = username;
		this.profil = profil;
	}

	public Client(String username) throws Exception {
		this.username = username.trim();
	}

	public User log(Connection connection) throws Exception {
		User user = null;
		boolean newClient = false;
		try {
			if (select(connection, "WHERE username='" + username + "'").isEmpty()) {
				newClient = true;
				setProfil("user");
				String idclient = insertAndReturnId(connection);
				connection.commit();
				user = new User(idclient, username, profil);
			} else {
				Client client = (Client) select(connection, "WHERE username='" + username + "'").getFirst();
				user = new User(client.getId(), client.getUsername(), client.getProfil());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (newClient == true) {
				Connector.Rollback(connection);
			}
		}

		return user;
	}

	public Client() throws Exception {

	}

	public Client(String username, String profil) throws Exception {
		setUsername(username.trim());
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
	public String getId() {
		return this.id;
	}

	@Setter(columnName = "id")
	public void setId(String id) {
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

	@Getter(columnName = "profil")
	public String getProfil() {
		return this.profil;
	}

	@Setter(columnName = "profil")
	public void setProfil(String profil) {
		this.profil = profil;
	}

}
