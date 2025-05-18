package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;


    public void creeCompteVendeur(Magasin magasin, String nom, String prenom){
        // Ajout d'un vendeur
    }

    public void ajouterNouvelleLibrairie(Magasin magasin) throws SQLException{
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM magasin WHERE idMagasin = " + magasin.getId());
        if(r.next()){
            throw new SQLException("Le magasin existe déjà");
        } 
        else {
            st.executeUpdate("INSERT INTO magasin (idmag, nommag, villemag) VALUES (" + magasin.getId() + ", '" + magasin.getNom() + "', '" + magasin.getVille() + "')");
        }
    }

    public void gererLesStocks(){
        // plusieurs méthodes
    }

    public void statVente(){
        // diagramme
    }
}
