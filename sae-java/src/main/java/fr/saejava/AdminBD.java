package fr.saejava;

import java.lang.Thread.State;
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
        pstmt = this.connexion.prepareStatement("insert into VENDEUR values ("+idVendeur+", '"+magasin+"'),");
        pstmt.executeUpdate();	
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
            throw new SQLException("Le magasin existe déjà");
        } 
        else {
            st.executeUpdate("INSERT INTO magasin (idmag, nommag, villemag) VALUES (" + magasin.getId() + ", '" + magasin.getNom() + "', '" + magasin.getVille() + "')");
        }
    }

    public void gererLesStocks(){
    
    }

    public void ajouterMagasin(Magasin magasin) throws SQLException {
        st = connexion.createStatement();
        String query = "INSERT INTO MAGASIN(idmag, nommag, villemag) VALUES (" + magasin.getId() + ", '" + magasin.getNom() + "', '" + magasin.getVille() + "')";
        st.executeUpdate(query);
        st.close();
    }

    public void modifierLivre(Livre l, int qte) throws SQLException{
        Statement st = connexion.createStatement();
        st.executeUpdate("INSERT INTO LIVRE (isbn, titre, auteur, editeur, annee, prix) VALUES ('" + l.getIsbn() + "', '" + l.getTitre() + "', '" + l.getAuteurs() + "', '" + l.getEditeurs() + "', " + l.getDatePubli() + ", " + l.getPrix() + ")");
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
            st.executeUpdate("UPDATE livre SET qte = "+ nouvelleQte +" WHERE isbn = '"+ l.getIsbn() +"'");
        } 
        else {
            st.executeUpdate("INSERT INTO livre (isbn, titre, auteur, editeur, annee, prix) VALUES ('" + l.getIsbn() + "', '" + l.getTitre() + "', '" + l.getAuteurs() + "', '" + l.getEditeurs() + "', " + l.getDatePubli() + ", " + l.getPrix() + ")");
        }
    }

    public void supprimerLivre(Livre livre) throws SQLException {
        st = connexion.createStatement();
        st.executeUpdate("DELETE FROM LIVRE WHERE isbn = '" + livre.getIsbn() + "'");
        st.executeUpdate("DELETE FROM POSSEDER WHERE isbn = '" + livre.getIsbn() + "'");
    }

    public void modifierLivre(Livre l) throws SQLException {
        PreparedStatement pstmt = this.connexion.prepareStatement("UPDATE LIVRE SET titre = ?, auteur = ?, editeur = ?, annee = ?, prix = ? WHERE isbn = ?");
        pstmt.setString(1, l.getTitre());
        pstmt.setString(2, l.getAuteurs());
        pstmt.setString(3, l.getEditeurs());
        pstmt.setString(4, l.getDatePubli());
        pstmt.setDouble(5, l.getPrix());
        pstmt.setString(6, l.getIsbn());
        pstmt.executeUpdate();
    }
    
    public void statVente(){
        // diagramme
    }

    public void creerCompteVendeur(int idVendeur, String nom, String prenom, String username, String motDePasse) throws SQLException {
         Statement st = connexion.createStatement();
         ResultSet r = st.executeQuery("SELECT * FROM utilisateur WHERE idUtilisateur =" + idVendeur);
         if(r.next()) 
         {throw new SQLException("L'utilisateur existe déjà");} 
         else {
            st.executeUpdate("INSERT INTO utilisateur (idUtilisateur, nom, prenom, username, motDePasse, role) VALUES (" + idVendeur + ", '" + nom + "', '" + prenom + "', '" + username + "', '" + motDePasse + "', 'vendeur')");
            ApplicationTerminal app = new ApplicationTerminal();
            Magasin magasin = app.menuChoisirMagasin();
            if (magasin != null) {
                Statement st1 = connexion.createStatement();
                st1.executeUpdate("INSERT INTO VENDEUR (idVendeur, idMagasin) VALUES (" + idVendeur + ", " + magasin.getId() + ")");
            }
    
    }
}}
