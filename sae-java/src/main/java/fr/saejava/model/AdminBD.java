package fr.saejava.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminBD {
    ConnexionMySQL connexion;

    public AdminBD(ConnexionMySQL connexion){
        this.connexion = connexion;
    }

    /**
     * Crée un compte vendeur dans la base de données
     * @param idVendeur l'identifiant du vendeur
     * @param nom le nom du vendeur
     * @param prenom le prénom du vendeur
     * @param username le nom d'utilisateur du vendeur
     * @param motDePasse le mot de passe du vendeur
     * @param magasin le magasin auquel le vendeur est associé
     * @throws SQLException pour gérer si il y a une erreur SQL
     */
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
     * @param magasin le magasin à ajouter
     * @throws SQLException pour gérer si il y a une erreur SQL
     */
    
    public void ajouterNouvelleLibrairie(Magasin magasin) throws SQLException{
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT * FROM magasin WHERE idMagasin = " + magasin.getId());
            if(r.next()){
                throw new SQLException("Le magasin existe déjà");
            } 
            else {
                st.executeUpdate("INSERT INTO magasin (idmag, nommag, villemag) VALUES (" + magasin.getId() + ", '" + magasin.getNom() + "', '" + magasin.getVille() + "')");
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
    }

    /**
     * Ajoute un magasin à la base de données
     * @param magasin le magasin à ajouter
     * @throws SQLException pour gérer si il y a une erreur SQL
     */
    public void ajouterMagasin(Magasin magasin) throws SQLException {
        Statement st = connexion.createStatement();
        try {
            String query = "INSERT INTO MAGASIN(idmag, nommag, villemag) VALUES (" + magasin.getId() + ", '" + magasin.getNom() + "', '" + magasin.getVille() + "')";
            st.executeUpdate(query);
        } finally {
            if (st != null) st.close();
        }
    }

    /**
     * Modifie un livre dans la base de données
=     * @param l le livre à modifier
     * @param qte la quantité de livres à modifier
     * @throws SQLException pour gérer si il y a une erreur SQL
     */
    public void modifierLivre(Livre l, int qte) throws SQLException{
        Statement st = connexion.createStatement();
        try {
            st.executeUpdate("INSERT INTO LIVRE (isbn, titre, auteur, editeur, annee, prix) VALUES ('" + l.getIsbn() + "', '" + l.getTitre() + "', '" + l.getAuteurs() + "', '" + l.getEditeurs() + "', " + l.getDatePubli() + ", " + l.getPrix() + ")");
        } finally {
            if (st != null) st.close();
        }
    }

    /**
     * ajoute un livre dans la base de données
     * @param l le livre à ajouter
     * @param qte la quantité de livres à ajouter
     * @throws SQLException pour gérer si il y a une erreur SQL
     */

    public void ajouteLivre(Livre l, int qte) throws SQLException {
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT * FROM livre WHERE isbn = '"+ l.getIsbn() +"'");
            if(r.next()){
                int qteActuelle = r.getInt("qte");
                int nouvelleQte = qteActuelle + qte;
                st.executeUpdate("UPDATE livre SET qte = "+ nouvelleQte +" WHERE isbn = '"+ l.getIsbn() +"'");
            } 
            else {
                st.executeUpdate("INSERT INTO livre (isbn, titre, auteur, editeur, annee, prix) VALUES ('" + l.getIsbn() + "', '" + l.getTitre() + "', '" + l.getAuteurs() + "', '" + l.getEditeurs() + "', " + l.getDatePubli() + ", " + l.getPrix() + ")");
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
    }

    /**
     * Supprime un livre de la base de données
     * @param livre le livre à supprimer
     * @throws SQLException pour gérer si il y a une erreur SQL
     */
    public void supprimerLivre(Livre livre) throws SQLException {
        Statement st = connexion.createStatement();
        try {
            st.executeUpdate("DELETE FROM LIVRE WHERE isbn = '" + livre.getIsbn() + "'");
            st.executeUpdate("DELETE FROM POSSEDER WHERE isbn = '" + livre.getIsbn() + "'");
        } finally {
            if (st != null) st.close();
        }
    }

    /**
     * Modifie les informations d'un livre dans la base de données
     * @param l le livre à modifier
     * @throws SQLException pour gérer si il y a une erreur SQL
     */
    public void modifierLivre(Livre l) throws SQLException {
        PreparedStatement pstmt = this.connexion.prepareStatement("UPDATE LIVRE SET titre = ?, annee = ?, prix = ? WHERE isbn = ?");
        try {
            pstmt.setString(1, l.getTitre());
            pstmt.setString(2, l.getDatePubli());
            pstmt.setDouble(3, l.getPrix());
            pstmt.setString(4, l.getIsbn());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    } 
     public void Requete1() throws SQLException {
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("select M.nommag as Magasin,YEAR(C.datecom) Annee,sum(D.qte) qte\r\n" +
                            "from MAGASIN M NATURAL JOIN COMMANDE C NATURAL JOIN DETAILCOMMANDE D\r\n" +
                            "group by Magasin,Annee;\r\n" +
                            "");
            while (r.next()) {
                System.out.println("Magasin: " + r.getString("Magasin") + ", Annee: " + r.getInt("Annee") + ", Quantite: " + r.getInt("qte"));
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
    }

    public void Requete2() throws SQLException {
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("Select nomclass as Theme, sum(prixvente*qte) as Montant\r\n" +
                            "from CLASSIFICATION NATURAL JOIN THEMES NATURAL JOIN LIVRE NATURAL JOIN DETAILCOMMANDE NATURAL JOIN COMMANDE\r\n" +
                            "WHERE YEAR(datecom) = '2024'\r\n" +
                            "group by LEFT(iddewey,1);");
            while (r.next()) {
                System.out.println("Theme: " + r.getString("Theme") + ", Montant: " + r.getDouble("Montant"));
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
    }

    public void Requete3() throws SQLException {
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("select YEAR(datecom) annee ,(REPLACE(REPLACE(enligne,'N','En magasin'),'O','En ligne')) typevente, round(sum(qte*prixvente)) montant\r\n" +
                            "from COMMANDE NATURAL JOIN DETAILCOMMANDE \r\n" +
                            "where year(datecom) != '2025'\r\n" +
                            "group by annee,typevente;");
            while (r.next()) {
                System.out.println("Année: " + r.getInt("annee") + ", Type de vente: " + r.getString("typevente") + ", Montant: " + r.getDouble("montant"));
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
    }

    public void Requete4() throws SQLException {
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("select month(C.datecom) mois,M.nommag as Magasin,sum(D.qte*prixvente) CA\r\n" +
                            "from MAGASIN M NATURAL JOIN COMMANDE C NATURAL JOIN DETAILCOMMANDE D\r\n" +
                            "where YEAR(datecom) ='2024'\r\n" +
                            "group by Magasin,mois;");
            while (r.next()) {
                System.out.println("Magasin: " + r.getString("Magasin") + ", Mois: " + r.getInt("mois") + ", Chiffre d'affaires: " + r.getDouble("CA"));
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
    }

    public void Requete5() throws SQLException {
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("select nommag as Magasin, sum(qte*prix) as ValeurStock\r\n" +
                            "from MAGASIN natural join POSSEDER natural join LIVRE\r\n" +
                            "group by Magasin;");
            while (r.next()) {
                System.out.println("Magasin: " + r.getString("Magasin") + ", Valeur du stock: " + r.getDouble("ValeurStock"));
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
    }

    public void Requete6() throws SQLException {
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("select annee, nomauteur, total\n" +
                            "from VentesParAuteur v1\n" +
                            "where total = (select MAX(total) as total\n" +
                            "from VentesParAuteur v2\n" +
                            "where v1.annee = v2.annee)\n" +
                            "group by annee;");
            while (r.next()) {
                System.out.println("Année: " + r.getInt("annee") + ", Auteur: " + r.getString("nomauteur") + ", Total: " + r.getInt("total"));
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
    }
}
