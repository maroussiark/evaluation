package com.project.modele;

import com.project.annotation.ColumnField;
import com.project.annotation.Getter;
import com.project.annotation.Setter;
import com.project.annotation.TableName;
import com.project.table.JDBC;
import com.project.tools.Formatter;

import java.sql.Connection;

@TableName(database = "btp", driver = "postgres", name = "detail_devis", password = "Maroussia1833", user = "postgres")
public class Detail_devis extends JDBC {

	@ColumnField(columnName = "id", primaryKey = true, defaultValue = true)
	Long id;
	@ColumnField(columnName = "iddevis")
	String iddevis;
	@ColumnField(columnName = "client")
	String client;
	@ColumnField(columnName = "travaux")
	String travaux;
	@ColumnField(columnName = "unite")
	String unite;
	@ColumnField(columnName = "quantite")
	Double quantite;
	@ColumnField(columnName = "pu")
	Double pu;
	@ColumnField(columnName = "montant")
	Double montant;
	@ColumnField(columnName = "taux")
	Double taux;
	@ColumnField(columnName = "finition")
	String finition;
	@ColumnField(columnName = "code_travaux")
	String code_travaux;

	public String getPuString() {
		return Formatter.formatThousand(pu);
	}

	public String getMontantString() {
		return Formatter.formatThousand(montant);
	}

	public Detail_devis() throws Exception {

	}

	public Detail_devis(String iddevis, String client, String travaux, Double quantite, Double pu, Double montant)
			throws Exception {
		setIddevis(iddevis.trim());
		setClient(client.trim());
		setTravaux(travaux.trim());
		setQuantite(quantite);
		setPu(pu);
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

	@Getter(columnName = "unite")
	public String getUnite() {
		return this.unite;
	}

	@Setter(columnName = "unite")
	public void setUnite(String unite) {
		this.unite = unite;
	}

	@Getter(columnName = "id")
	public Long getId() {
		return this.id;
	}

	@Setter(columnName = "id")
	public void setId(Long id) {
		this.id = id;
	}

	@Getter(columnName = "iddevis")
	public String getIddevis() {
		return this.iddevis;
	}

	@Setter(columnName = "iddevis")
	public void setIddevis(String iddevis) {
		this.iddevis = iddevis;
	}

	@Getter(columnName = "client")
	public String getClient() {
		return this.client;
	}

	@Setter(columnName = "client")
	public void setClient(String client) {
		this.client = client;
	}

	@Getter(columnName = "travaux")
	public String getTravaux() {
		return this.travaux;
	}

	@Setter(columnName = "travaux")
	public void setTravaux(String travaux) {
		this.travaux = travaux;
	}

	@Getter(columnName = "quantite")
	public Double getQuantite() {
		return this.quantite;
	}

	@Setter(columnName = "quantite")
	public void setQuantite(Double quantite) {
		this.quantite = quantite;
	}

	@Getter(columnName = "pu")
	public Double getPu() {
		return this.pu;
	}

	@Setter(columnName = "pu")
	public void setPu(Double pu) {
		this.pu = pu;
	}

	@Getter(columnName = "montant")
	public Double getMontant() {
		return this.montant;
	}

	@Setter(columnName = "montant")
	public void setMontant(Double montant) {
		this.montant = montant;
	}

	@Getter(columnName = "taux")
	public Double getTaux() {
		return this.taux;
	}

	@Setter(columnName = "taux")
	public void setTaux(Double taux) {
		this.taux = taux;
	}

	@Getter(columnName = "finition")
	public String getFinition() {
		return this.finition;
	}

	@Setter(columnName = "finition")
	public void setFinition(String finition) {
		this.finition = finition;
	}

	@Getter(columnName = "code_travaux")
	public String getCode_travaux() {
		return this.code_travaux;
	}

	@Setter(columnName = "code_travaux")
	public void setCode_travaux(String code_travaux) {
		this.code_travaux = code_travaux;
	}

}
