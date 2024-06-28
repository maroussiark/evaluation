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

@TableName(database = "btp", driver = "postgres", name = "csv_paiement", password = "Maroussia1833", user = "postgres")
public class Csv_paiement extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	Long id;
	@ColumnField(columnName = "ref_devis")
	String ref_devis;
	@ColumnField(columnName = "ref_paiement")
	String ref_paiement;
	@ColumnField(columnName = "date_paiement")
	Date date_paiement;
	@ColumnField(columnName = "montant")
	Double montant;
	LinkedList<Erreur_data> erreurs = new LinkedList<Erreur_data>();
	int ligne;
	LinkedList<String> refpaiement = new LinkedList<>();

	public LinkedList<String> getrefPaiement(Connection connection) throws Exception {
		LinkedList<String> refs = new LinkedList<>();
		LinkedList<Paiement> paiements = new Paiement().select(connection);
		for (Paiement paiement : paiements) {
			refs.add(paiement.getRef());
		}
		return refs;
	}

	public boolean save_data(MultipartFile file, Connection connection) throws Exception {
		boolean erreurExist = false;
		InputStream inputStream = file.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		reader.readLine();
		int numero = 1;
		refpaiement.addAll(getrefPaiement(connection));
		// refpaiement.add("");

		PreparedStatement preparedStatement2 = null;
		String sql2 = "DELETE FROM csv_paiement ";
		preparedStatement2 = connection.prepareStatement(sql2);
		preparedStatement2.executeUpdate();

		while ((line = reader.readLine()) != null) {
			// String[] data = line.split(";");
			String[] data = Formatter.parseCSVLine(line);

			numero = numero + 1;
			setLigne(numero);
			Csv_paiement paiement = new Csv_paiement();

			System.out.println(data[1]);

			if (refpaiement.contains(data[1])) {
				paiement.getErreurs()
						.add(new Erreur_data(ligne, "Reference Paiement deja existant", file.getOriginalFilename()));
			}

			refpaiement.add(data[1]);

			paiement.setRef_devis(data[0]);
			paiement.setRef_paiement(data[1]);
			String date_paiement = Formatter.formatDate(data[2], "dd/MM/yyyy", "yyyy-MM-dd");
			paiement.setDate_paiement(date_paiement);
			paiement.setMontant(Double.parseDouble(data[3].replaceAll(",", ".")));

			if (paiement.getErreurs().isEmpty()) {
				paiement.insert(connection);
			} else {
				erreurExist = true;
				paiement.saveErreur(connection);
			}
		}

		PreparedStatement preparedStatement1 = null;

		try {

			String sql1 = "INSERT INTO paiement(iddevis,montant,date,ref) SELECT ref_devis,montant,date_paiement,ref_paiement FROM csv_paiement";
			preparedStatement1 = connection.prepareStatement(sql1);
			preparedStatement1.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			Connector.CloseStatement(preparedStatement1);
			Connector.CloseStatement(preparedStatement2);
		}
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

	public Csv_paiement() throws Exception {

	}

	public Csv_paiement(String ref_devis, String ref_paiement, Date date_paiement, Double montant) throws Exception {
		setRef_devis(ref_devis.trim());
		setRef_paiement(ref_paiement.trim());
		setDate_paiement(date_paiement);
		setMontant(montant);
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

	@Getter(columnName = "ref_devis")
	public String getRef_devis() {
		return this.ref_devis;
	}

	@Setter(columnName = "ref_devis")
	public void setRef_devis(String ref_devis) {
		this.ref_devis = ref_devis;
	}

	@Getter(columnName = "ref_paiement")
	public String getRef_paiement() {
		return this.ref_paiement;
	}

	@Setter(columnName = "ref_paiement")
	public void setRef_paiement(String ref_paiement) {
		this.ref_paiement = ref_paiement;
	}

	@Getter(columnName = "date_paiement")
	public Date getDate_paiement() {
		return this.date_paiement;
	}

	@Setter(columnName = "date_paiement")
	public void setDate_paiement(Date date_paiement) {
		this.date_paiement = date_paiement;
	}

	public void setDate_paiement(String date_paiement) {
		Date daty_temp = Date.valueOf(date_paiement.trim());
		setDate_paiement(daty_temp);
	}

	@Getter(columnName = "montant")
	public Double getMontant() {
		return this.montant;
	}

	@Setter(columnName = "montant")
	public void setMontant(Double montant) {
		this.montant = montant;
	}

}
