package com.project.modele;
import com.project.annotation.ColumnField; 
import com.project.annotation.Getter; 
import com.project.annotation.Setter; 
import com.project.annotation.TableName; 
import com.project.table.JDBC; 
import java.sql.Connection; 


@TableName(database = "btp", driver = "postgres", name = "erreur_data", password = "Maroussia1833", user = "postgres")
public class Erreur_data extends JDBC {

    @ColumnField(columnName = "id" ,primaryKey = true, defaultValue = true ) 
Long id;
    @ColumnField(columnName = "ligne" ) 
Integer ligne;
    @ColumnField(columnName = "erreur" ) 
String erreur;
    @ColumnField(columnName = "nom_fichier" ) 
String nom_fichier;
    

    public Erreur_data() throws Exception{

    }

    public Erreur_data(Integer ligne ,String erreur ,String nom_fichier)throws Exception{
setLigne( ligne); 
setErreur(erreur.trim()); 
setNom_fichier(nom_fichier.trim()); 
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
      	this.id=id;
	} 

	 @Getter(columnName = "ligne")
	public Integer getLigne() {
      	return this.ligne; 
	} 

	 @Setter(columnName = "ligne")
	public void setLigne(Integer ligne) { 
      	this.ligne=ligne;
	} 

	 @Getter(columnName = "erreur")
	public String getErreur() {
      	return this.erreur; 
	} 

	 @Setter(columnName = "erreur")
	public void setErreur(String erreur) { 
      	this.erreur=erreur;
	} 

	 @Getter(columnName = "nom_fichier")
	public String getNom_fichier() {
      	return this.nom_fichier; 
	} 

	 @Setter(columnName = "nom_fichier")
	public void setNom_fichier(String nom_fichier) { 
      	this.nom_fichier=nom_fichier;
	} 




}
