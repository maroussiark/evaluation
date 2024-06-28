package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.connection.Connector;
import com.project.table.JDBC;
import com.project.tools.Formatter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.LinkedList;

import org.springframework.web.multipart.MultipartFile;

@TableName(database = "btp", driver = "postgres", name = "csv_devis", password = "Maroussia1833", user = "postgres")
public class Csv_devis extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	Long id;
	@ColumnField(columnName = "client")
	String client;
	@ColumnField(columnName = "ref_devis")
	String ref_devis;
	@ColumnField(columnName = "type_maison")
	String type_maison;
	@ColumnField(columnName = "finition")
	String finition;
	@ColumnField(columnName = "taux_finition")
	Double taux_finition;
	@ColumnField(columnName = "date_devis")
	Date date_devis;
	@ColumnField(columnName = "date_debut")
	Date date_debut;
	@ColumnField(columnName = "lieu")
	String lieu;
	LinkedList<Erreur_data> erreurs = new LinkedList<Erreur_data>();
	int ligne;

	public boolean save_data(MultipartFile file, Connection connection) throws Exception {
		boolean erreurExist = false;
		InputStream inputStream = file.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		reader.readLine();
		int numero = 1;
		while ((line = reader.readLine()) != null) {
			// String[] data = line.split(";");
			String[] data = Formatter.parseCSVLine(line);

			numero = numero + 1;
			setLigne(numero);

			Csv_devis csv_devis = new Csv_devis();
			csv_devis.setClient(data[0]);
			csv_devis.setRef_devis(data[1]);
			csv_devis.setType_maison(data[2]);
			csv_devis.setFinition(data[3]);

			String taux = data[4].replace("%", "").replace(",", ".");
			csv_devis.setTaux_finition(Double.valueOf(taux));

			String date_devis = Formatter.formatDate(data[5], "dd/MM/yyyy", "yyyy-MM-dd");
			csv_devis.setDate_devis(date_devis);

			String date_debut = Formatter.formatDate(data[6], "dd/MM/yyyy", "yyyy-MM-dd");
			csv_devis.setDate_debut(date_debut);

			csv_devis.setLieu(data[7]);

			if (csv_devis.getErreurs().isEmpty()) {
				csv_devis.insert(connection);
			} else {
				erreurExist = true;
				csv_devis.saveErreur(connection);
			}
		}
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;

		// try {
		// String sql1 = "INSERT INTO client(username) SELECT distinct(client) FROM
		// csv_devis";
		// preparedStatement1 = connection.prepareStatement(sql1);
		// preparedStatement1.executeUpdate();

		// String sql2 = "INSERT INTO finition(nom,pourcentage) SELECT
		// distinct(finition),taux_finition FROM csv_devis";
		// preparedStatement2 = connection.prepareStatement(sql2);
		// preparedStatement2.executeUpdate();

		// String sql4 = "INSERT INTO
		// devis(id,idclient,idmaison,idfinition,date,datedebut,lieu) SELECT
		// ref_devis,client.id,maison.id,finition.id,date_devis,date_debut,lieu FROM
		// csv_devis JOIN client on client.username = csv_devis.client JOIN maison ON
		// maison.nom = csv_devis.type_maison JOIN finition ON finition.nom =
		// csv_devis.finition";
		// preparedStatement4 = connection.prepareStatement(sql4);
		// preparedStatement4.executeUpdate();

		// String sql3 = "INSERT INTO
		// detail_devis(iddevis,client,travaux,unite,quantite,pu,montant,taux,finition,code_travaux)
		// SELECT * FROM detail_devis_1";
		// preparedStatement3 = connection.prepareStatement(sql3);
		// preparedStatement3.executeUpdate();

		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// } finally {

		// Connector.CloseStatement(preparedStatement4);
		// Connector.CloseStatement(preparedStatement3);
		// Connector.CloseStatement(preparedStatement2);
		// Connector.CloseStatement(preparedStatement1);
		// }
		return erreurExist;
	}

	public void saveErreur(Connection connection) throws Exception {
		for (Erreur_data erreur : erreurs) {
			erreur.insert(connection);
		}
	}

	public LinkedList<Erreur_data> getErreurs() {
		return this.erreurs;
	}

	public void setErreurs(LinkedList<Erreur_data> erreurs) {
		this.erreurs = erreurs;
	}

	public int getLigne() {
		return this.ligne;
	}

	public void setLigne(int ligne) {
		this.ligne = ligne;
	}

	public Csv_devis() throws Exception {

	}

	public Csv_devis(String client, String ref_devis, String type_maison, String finition, Double taux_finition,
			Date date_devis, Date date_debut, String lieu) throws Exception {
		setClient(client.trim());
		setRef_devis(ref_devis.trim());
		setType_maison(type_maison.trim());
		setFinition(finition.trim());
		setTaux_finition(taux_finition);
		setDate_devis(date_devis);
		setDate_debut(date_debut);
		setLieu(lieu.trim());
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

	@Getter(columnName = "client")
	public String getClient() {
		return this.client;
	}

	@Setter(columnName = "client")
	public void setClient(String client) {
		this.client = client;
	}

	@Getter(columnName = "ref_devis")
	public String getRef_devis() {
		return this.ref_devis;
	}

	@Setter(columnName = "ref_devis")
	public void setRef_devis(String ref_devis) {
		this.ref_devis = ref_devis;
	}

	@Getter(columnName = "type_maison")
	public String getType_maison() {
		return this.type_maison;
	}

	@Setter(columnName = "type_maison")
	public void setType_maison(String type_maison) {
		this.type_maison = type_maison;
	}

	@Getter(columnName = "finition")
	public String getFinition() {
		return this.finition;
	}

	@Setter(columnName = "finition")
	public void setFinition(String finition) {
		this.finition = finition;
	}

	@Getter(columnName = "taux_finition")
	public Double getTaux_finition() {
		return this.taux_finition;
	}

	@Setter(columnName = "taux_finition")
	public void setTaux_finition(Double taux_finition) {
		this.taux_finition = taux_finition;
	}

	@Getter(columnName = "date_devis")
	public Date getDate_devis() {
		return this.date_devis;
	}

	@Setter(columnName = "date_devis")
	public void setDate_devis(Date date_devis) {
		this.date_devis = date_devis;
	}

	public void setDate_devis(String date_devis) {
		Date daty_temp = Date.valueOf(date_devis.trim());
		setDate_devis(daty_temp);
	}

	@Getter(columnName = "date_debut")
	public Date getDate_debut() {
		return this.date_debut;
	}

	@Setter(columnName = "date_debut")
	public void setDate_debut(Date date_debut) {
		this.date_debut = date_debut;
	}

	public void setDate_debut(String date_debut) {
		Date daty_temp = Date.valueOf(date_debut.trim());
		setDate_debut(daty_temp);
	}

	@Getter(columnName = "lieu")
	public String getLieu() {
		return this.lieu;
	}

	@Setter(columnName = "lieu")
	public void setLieu(String lieu) {
		this.lieu = lieu;
	}

}
