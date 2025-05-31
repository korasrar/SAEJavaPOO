package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;

    public AdminBD(ConnexionMySQL connexion){
        this.connexion = connexion;
    }

    public void creeCompteVendeur(Magasin magasin, String nom, String prenom) throws SQLException{
        st = connexion.createStatement();
        

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

    public void ajouteLivre(Livre l, int qte) throws SQLException {
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM livre WHERE isbn = '"+ l.getIsbn() +"'");
        if(r.next()){
            int qteActuelle = r.getInt("qte");
            int nouvelleQte = qteActuelle + qte;
            st.executeUpdate("UPDATE livre SET qte = "+ nouvelleQte +" WHERE isbn = '"+ l.getIsbn() +"'");
        } 
        else {
            st.executeUpdate("INSERT INTO livre (isbn, titre, auteur, editeur, annee, prix) VALUES ('" + l.getIsbn() + "', '" + l.getTitre() + "', '" + l.getAuteurs() + "', '" + l.getEditeurs() + "', " + l.getDatePubli() + ", " + l.getPrix() + ")");
        }
    } // code redondant, à revoir

    public void statVente(){
        // diagramme
    }
}
