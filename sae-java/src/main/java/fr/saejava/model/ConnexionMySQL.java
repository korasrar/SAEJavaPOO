package fr.saejava.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnexionMySQL {
    // Connection avec JDBC 
    private Connection mysql=null;
	private boolean connecte=false;

	public ConnexionMySQL() throws ClassNotFoundException{
        Class.forName("org.mariadb.jdbc.Driver");
    }

    public void connecter(String nomServeur, String nomBase, String nomLogin, String motDePasse) throws SQLException {
        System.out.println("Tentative de connection...");
        try {
            mysql=DriverManager.getConnection("jdbc:mariadb://"+nomServeur+":3306/"+nomBase,nomLogin,motDePasse);
            System.out.println("Connection réussi");
            } catch (SQLException ex){
                System.out.println("Voici le message d'erreur : "+ex.getMessage()+
                ex.getErrorCode());
            }
        this.connecte=this.mysql!=null;
    }
    
	public void close() throws SQLException {
		this.connecte=false;
	}

    	public boolean isConnecte() { return this.connecte;}
	public Statement createStatement() throws SQLException {
		return this.mysql.createStatement();
	}

	public PreparedStatement prepareStatement(String requete) throws SQLException{
		return this.mysql.prepareStatement(requete);
	}
}
