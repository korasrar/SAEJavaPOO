package fr.saejava;

import java.sql.PreparedStatement;
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

    public void creeCompteVendeur(int idVendeur, String nom, String prenom,String username, String motDePasse, Magasin magasin) throws SQLException{
        PreparedStatement pstmt = this.connexion.prepareStatement("insert into UTILISATEUR values ("+idVendeur+",'"+nom+"','"+prenom+"','"+username+"','"+motDePasse+"','vendeur'),");
        pstmt.executeUpdate();
        pstmt.close();
        pstmt = this.connexion.prepareStatement("insert into VENDEUR values ("+idVendeur+", '"+magasin+"'),");
        pstmt.executeUpdate();
        pstmt.close();
    }

    /**
     * ajoute une nouvelle librairie dans la base de données
     * 
     * @param magasin le magasin à ajouter
     * @throws SQLException pour gérer si il y a une erreur SQL
     */
    
    public void ajouterNouvelleLibrairie(Magasin magasin) throws SQLException{
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM magasin WHERE idMagasin = " + magasin.getId());
        if(r.next()){
            r.close();
            st.close();
            throw new SQLException("Le magasin existe déjà");
        } 
        else {
            r.close();
            st.executeUpdate("INSERT INTO magasin (idmag, nommag, villemag) VALUES (" + magasin.getId() + ", '" + magasin.getNom() + "', '" + magasin.getVille() + "')");
            st.close();
        }
    }

    public void gererLesStocks(){
    
    }

    /**
     * ajoute un livre dans la base de données
     * 
     * @param l le livre à ajouter
     * @param qte la quantité de livres à ajouter
     * @throws SQLException pour gérer si il y a une erreur SQL
     */

    public void ajouteLivre(Livre l, int qte) throws SQLException {
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM livre WHERE isbn = '"+ l.getIsbn() +"'");
        if(r.next()){
            int qteActuelle = r.getInt("qte");
            int nouvelleQte = qteActuelle + qte;
            r.close();
            st.executeUpdate("UPDATE livre SET qte = "+ nouvelleQte +" WHERE isbn = '"+ l.getIsbn() +"'");
            st.close();
        } 
        else {
            r.close();
            st.executeUpdate("INSERT INTO livre (isbn, titre, auteur, editeur, annee, prix) VALUES ('" + l.getIsbn() + "', '" + l.getTitre() + "', '" + l.getAuteurs() + "', '" + l.getEditeurs() + "', " + l.getDatePubli() + ", " + l.getPrix() + ")");
            st.close();
        }
    } // code redondant, à revoir

    public void statVente(){
        // diagramme
    }
}
