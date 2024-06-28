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
import java.sql.PreparedStatement;
import java.util.LinkedList;

import org.springframework.web.multipart.MultipartFile;

@TableName(database = "btp", driver = "postgres", name = "csv_travaux_maison", password = "Maroussia1833", user = "postgres")
public class Csv_travaux_maison extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	Long id;
	@ColumnField(columnName = "type_maison")
	String type_maison;
	@ColumnField(columnName = "description")
	String description;
	@ColumnField(columnName = "surface")
	Double surface;
	@ColumnField(columnName = "code_travaux")
	String code_travaux;
	@ColumnField(columnName = "type_travaux")
	String type_travaux;
	@ColumnField(columnName = "unite")
	String unite;
	@ColumnField(columnName = "prix_unitaire")
	Double prix_unitaire;
	@ColumnField(columnName = "quantite")
	Double quantite;
	@ColumnField(columnName = "duree_travaux")
	Integer duree_travaux;
	LinkedList<Erreur_data> erreurs = new LinkedList<Erreur_data>();
	int ligne;
	LinkedList<String> listTravaux = new LinkedList<>();
	LinkedList<String> maisonsErreurs = new LinkedList<>();

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
			Csv_travaux_maison travaux = new Csv_travaux_maison();

			String lignetravaux = data[0] + data[3];
			if (listTravaux.contains(lignetravaux)) {
				System.out.println(true);
				Erreur_data erreur_data = new Erreur_data(ligne, "Code Travaux dupliquer", file.getOriginalFilename());
				travaux.getErreurs().add(erreur_data);
				maisonsErreurs.add(data[0]);
			}

			listTravaux.add(lignetravaux);
			System.out.println(travaux.getErreurs().size());

			travaux.setType_maison(data[0]);
			travaux.setDescription(data[1]);
			// travaux.setDescription(data[1].replaceAll("'", "''"));
			travaux.setSurface(Double.parseDouble(data[2].replace(",", ".")));
			travaux.setCode_travaux(data[3]);
			travaux.setType_travaux(data[4]);
			travaux.setUnite(data[5]);
			travaux.setPrix_unitaire(Double.parseDouble(data[6].replace(",", ".")));
			travaux.setQuantite(Double.parseDouble(data[7].replace(",", ".")));
			travaux.setDuree_travaux(Integer.valueOf(data[8]));

			if (travaux.getErreurs().isEmpty()) {
				travaux.insert(connection);
			} else {
				erreurExist = true;
				travaux.saveErreur(connection);
			}
		}
		for (String maisons : maisonsErreurs) {
			deleteRows(maisons, connection);
		}
		deleteRows(line, connection);
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;
		PreparedStatement preparedStatement5 = null;
		PreparedStatement preparedStatement6 = null;
		// try {
		// String sql1 = "INSERT INTO maison(nom) SELECT distinct(type_maison) FROM
		// csv_travaux_maison";
		// preparedStatement1 = connection.prepareStatement(sql1);
		// preparedStatement1.executeUpdate();

		// String sql2 = "INSERT INTO desc_maison (idmaison,description,surface) SELECT
		// distinct(maison.id),description,surface FROM csv_travaux_maison JOIN maison
		// on csv_travaux_maison.type_maison = maison.nom";
		// preparedStatement2 = connection.prepareStatement(sql2);
		// preparedStatement2.executeUpdate();

		// String sql4 = "INSERT INTO unite(nom) SELECT distinct(unite) FROM
		// csv_travaux_maison";
		// preparedStatement4 = connection.prepareStatement(sql4);
		// preparedStatement4.executeUpdate();

		// String sql5 = "INSERT INTO typeTravaux (id,nom,idunite,pu) SELECT
		// distinct(code_travaux),type_travaux,unite.id,prix_unitaire FROM
		// csv_travaux_maison JOIN unite on unite.nom = csv_travaux_maison.unite";
		// preparedStatement5 = connection.prepareStatement(sql5);
		// preparedStatement5.executeUpdate();

		// String sql6 = "INSERT INTO duree(idmaison,valeur) SELECT
		// distinct(maison.id),duree_travaux FROM csv_travaux_maison JOIN maison ON
		// maison.nom = csv_travaux_maison.type_maison";
		// preparedStatement6 = connection.prepareStatement(sql6);
		// preparedStatement6.executeUpdate();

		// String sql3 = "INSERT INTO travauxmaison(idmaison,idtravaux,quantite) SELECT
		// maison.id,typeTravaux.id,quantite FROM csv_travaux_maison JOIN maison on
		// maison.nom = csv_travaux_maison.type_maison JOIN typeTravaux on
		// typeTravaux.nom = csv_travaux_maison.type_travaux";
		// preparedStatement3 = connection.prepareStatement(sql3);
		// preparedStatement3.executeUpdate();

		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// } finally {
		// Connector.CloseStatement(preparedStatement6);
		// Connector.CloseStatement(preparedStatement5);
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

	public void deleteRows(String idmaison, Connection connection) throws Exception {
		String sql = "DELETE FROM csv_travaux_maison where type_maison='" + idmaison + "'";
		PreparedStatement preparedStatement1 = null;
		preparedStatement1 = connection.prepareStatement(sql);
		preparedStatement1.executeUpdate();
		Connector.CloseStatement(preparedStatement1);

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

	public Csv_travaux_maison() throws Exception {

	}

	public Csv_travaux_maison(String type_maison, String description, Double surface, String code_travaux,
			String type_travaux, String unite, Double prix_unitaire, Double quantite, Integer duree_travaux)
			throws Exception {
		setType_maison(type_maison.trim());
		setDescription(description.trim());
		setSurface(surface);
		setCode_travaux(code_travaux.trim());
		setType_travaux(type_travaux.trim());
		setUnite(unite.trim());
		setPrix_unitaire(prix_unitaire);
		setQuantite(quantite);
		setDuree_travaux(duree_travaux);
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

	@Getter(columnName = "type_maison")
	public String getType_maison() {
		return this.type_maison;
	}

	@Setter(columnName = "type_maison")
	public void setType_maison(String type_maison) {
		this.type_maison = type_maison;
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

	@Getter(columnName = "code_travaux")
	public String getCode_travaux() {
		return this.code_travaux;
	}

	@Setter(columnName = "code_travaux")
	public void setCode_travaux(String code_travaux) {
		this.code_travaux = code_travaux;
	}

	@Getter(columnName = "type_travaux")
	public String getType_travaux() {
		return this.type_travaux;
	}

	@Setter(columnName = "type_travaux")
	public void setType_travaux(String type_travaux) {
		this.type_travaux = type_travaux;
	}

	@Getter(columnName = "unite")
	public String getUnite() {
		return this.unite;
	}

	@Setter(columnName = "unite")
	public void setUnite(String unite) {
		this.unite = unite;
	}

	@Getter(columnName = "prix_unitaire")
	public Double getPrix_unitaire() {
		return this.prix_unitaire;
	}

	@Setter(columnName = "prix_unitaire")
	public void setPrix_unitaire(Double prix_unitaire) {
		this.prix_unitaire = prix_unitaire;
	}

	@Getter(columnName = "quantite")
	public Double getQuantite() {
		return this.quantite;
	}

	@Setter(columnName = "quantite")
	public void setQuantite(Double quantite) {
		this.quantite = quantite;
	}

	@Getter(columnName = "duree_travaux")
	public Integer getDuree_travaux() {
		return this.duree_travaux;
	}

	@Setter(columnName = "duree_travaux")
	public void setDuree_travaux(Integer duree_travaux) {
		this.duree_travaux = duree_travaux;
	}

}
