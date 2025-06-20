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

	/**
	 * pour connecter tout à la base de données MySQL
	 * @param nomServeur le nom du serveur 
	 * @param nomBase le nom de la base 
	 * @param nomLogin le nom d'utilisateur 
	 * @param motDePasse le mot de passe 
	 * @throws SQLException si une erreur SQL se produit lors de la connexion
	 */
    public void connecter(String nomServeur, String nomBase, String nomLogin, String motDePasse) throws SQLException {
        System.out.println("Tentative de connection...");
        try {
            mysql=DriverManager.getConnection("jdbc:mariadb://servinfo-maria:3306/DBmaubert","maubert","maubert");
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
