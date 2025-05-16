package fr.saejava;

import java.sql.*;
import java.util.Properties;

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
            mysql=DriverManager.getConnection(
            "jdbc:mysql://"+nomServeur+":3306/"+nomBase,
            nomLogin,motDePasse);
            System.out.println("Connection réussi");
            } catch (SQLException ex){
                System.out.println("Msg:"+ex.getMessage()+
                ex.getErrorCode());
            }
		this.connecte=this.mysql!=null;
	}
    
	public void close() throws SQLException {
		// fermer la connexion
		this.connecte=false;
	}

    	public boolean isConnecte() { return this.connecte;}
	public Statement createStatement() throws SQLException {
		return this.mysql.createStatement();
	}

	public PreparedStatement prepareStatement(String requete) throws SQLException{
		return this.mysql.prepareStatement(requete);
	}
    public static void main(String[] args) throws SQLException, ClassNotFoundException{
        
    }

    // Application avec menu 
}
