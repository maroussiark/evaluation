package com.project.modele;
import com.project.annotation.ColumnField; 
import com.project.annotation.Getter; 
import com.project.annotation.Setter; 
import com.project.annotation.TableName; 
import com.project.table.JDBC; 
import java.sql.Connection; 


@TableName(database = "btp", driver = "postgres", name = "unite", password = "Maroussia1833", user = "postgres")
public class Unite extends JDBC {

    @ColumnField(columnName = "id" ,primaryKey = true, defaultValue = true ) 
String id;
    @ColumnField(columnName = "nom" ) 
String nom;
    @ColumnField(columnName = "equivalence" ) 
Double equivalence;
    

    public Unite() throws Exception{

    }

    public Unite(String nom ,Double equivalence)throws Exception{
setNom(nom.trim()); 
setEquivalence( equivalence); 
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

	 @Getter(columnName = "equivalence")
	public Double getEquivalence() {
      	return this.equivalence; 
	} 

	 @Setter(columnName = "equivalence")
	public void setEquivalence(Double equivalence) { 
      	this.equivalence=equivalence;
	} 




}
