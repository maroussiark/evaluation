package com.project.modele;
import com.project.annotation.ColumnField; 
import com.project.annotation.Getter; 
import com.project.annotation.Setter; 
import com.project.annotation.TableName; 
import com.project.table.JDBC; 
import java.sql.Connection; 


@TableName(database = "btp", driver = "postgres", name = "travaux", password = "Maroussia1833", user = "postgres")
public class Travaux extends JDBC {

    @ColumnField(columnName = "id" ,primaryKey = true, defaultValue = true ) 
String id;
    @ColumnField(columnName = "nom" ) 
String nom;
    

    public Travaux() throws Exception{

    }

    public Travaux(String nom)throws Exception{
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
      	this.id=id;
	} 

	 @Getter(columnName = "nom")
	public String getNom() {
      	return this.nom; 
	} 

	 @Setter(columnName = "nom")
	public void setNom(String nom) { 
      	this.nom=nom;
	} 




}
