package fr.saejava;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class LivreBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;

    public LivreBD(ConnexionMySQL connexion) {
        this.connexion = connexion;
    }

    public Map<Livre,Boolean> rechercherLivre(Filtre filtre, String isbn, String titre, String auteur, VendeurBD connexionVendeur) throws SQLException {
        st = connexion.createStatement();
        Map<Livre,Boolean> livres = new HashMap<>();
        String stringBuilder = "SELECT * FROM LIVRE ";
        
        switch (filtre) {
            case isbn:
                stringBuilder += "WHERE isbn='"+isbn+"'";
                break;
            case auteur:
                stringBuilder += "NATURAL JOIN ECRIRE NATURAL JOIN AUTEUR WHERE nomauteur LIKE '%" + auteur + "%' OR levenshtein('"+auteur+"', nomauteur) between 0 and 2";
                break;
            case titre:
                stringBuilder += "WHERE titre LIKE '%" + titre + "%' OR levenshtein('"+titre+"', titre) between 0 and 2";
                break;
        }
        r = st.executeQuery(stringBuilder);
        while(r.next()){
            Livre livre = new Livre(r.getString("isbn"), r.getString("titre"), r.getInt("nbpages"), r.getString("datepubli"), r.getDouble("prix"));
            livres.put(livre, connexionVendeur.verifierDispo(livre));
        }
        r.close();
        st.close();
        return livres;
    }

    public List<Auteur> rechercheAuteur(String nomAuteur) throws SQLException{
        List<Auteur> auteurs = new ArrayList<>();
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM AUTEUR WHERE nomauteur LIKE '%"+ nomAuteur +"%' OR levenshtein('" + nomAuteur + "', nomauteur) between 0 and 2");
        while (r.next()) {
            auteurs.add(new Auteur(r.getInt("idauteur"), r.getString("nomauteur")));
        }
        if (auteurs.isEmpty()) {
            return null;
        } else {
            return auteurs;
        }
    }

    public List<Editeur> rechercheEditeur(String nomEditeur) throws SQLException {
        List<Editeur> editeurs = new ArrayList<>();
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM EDITEUR WHERE nomedit LIKE '%" +nomEditeur + "%' OR levenshtein('" + nomEditeur + "', nomedit) between 0 and 2");
        while (r.next()) {
            editeurs.add(new Editeur(r.getInt("idedit"), r.getString("nomedit")));
        }  
        if (editeurs.isEmpty()) {
            return null;
        } else {
            return editeurs;
        }
    }

    public List<Classification> rechercheClassification(String nomClassification) throws SQLException {
        List<Classification> classifications = new ArrayList<>();
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM CLASSIFICATION WHERE nomclass LIKE '%" + nomClassification + "%' OR levenshtein('" + nomClassification + "', nomclass) between 0 and 2");
        while (r.next()) {
            classifications.add(new Classification(r.getInt("iddewey"), r.getString("nomclass")));
        }
        if (classifications.isEmpty()) {
            return null;
        } else {
            return classifications;
        }
    }

    public void insererNouvelAuteur(Auteur auteur) throws SQLException {
        PreparedStatement pstmt = connexion.prepareStatement("SELECT * FROM AUTEUR WHERE nomauteur = ? and idauteur IS NOT NULL");
        pstmt.setString(1, auteur.getNomAuteur());
        r = pstmt.executeQuery();
        if (r.next()) {
            throw new SQLException("L'auteur existe déjà");
        } else {
            PreparedStatement pstmt2 = connexion.prepareStatement("INSERT INTO AUTEUR (nomauteur) VALUES (?)");
            pstmt2.setString(1, auteur.getNomAuteur());
            pstmt2.executeUpdate();
        }
    }

    public void insererNouvelEditeur(Editeur editeur) throws SQLException {
        PreparedStatement pstmt = connexion.prepareStatement("SELECT * FROM EDITEUR WHERE nomedit = ?");
        pstmt.setString(1, editeur.getNomEdit());
        r = pstmt.executeQuery();
        if (r.next()) {
            throw new SQLException("L'éditeur existe déjà");
        } else {
            PreparedStatement pstmt2 = connexion.prepareStatement("INSERT INTO EDITEUR (nomedit) VALUES (?)");
            pstmt2.setString(1, editeur.getNomEdit());
            pstmt2.executeUpdate();
        }
    }

    public void insererNouvelleClassification(Classification classification) throws SQLException {
        PreparedStatement pstmt = connexion.prepareStatement("SELECT * FROM CLASSIFICATION WHERE nomclass = ?");
        pstmt.setString(1, classification.getNomClass());
        r = pstmt.executeQuery();
        if (r.next()) {
            throw new SQLException("La classification existe déjà");
        } else {
            pstmt = connexion.prepareStatement("INSERT INTO CLASSIFICATION (nomclass) VALUES (?)");
            pstmt.setString(1, classification.getNomClass());
            pstmt.executeUpdate();
        }
    }

    /**
     * Récupère le dernier ID d'auteur dans la base de données
     * @return Le dernier ID d'auteur ou 0 si aucun auteur n'existe
     * @throws SQLException Si erreur SQL
     */
    public int getDernierIDAuteur() throws SQLException {
        st = connexion.createStatement();
        r = st.executeQuery("SELECT MAX(idauteur) AS dernierID FROM AUTEUR");
        if (r.next()) {
            return r.getInt("dernierID");
        } else {
            return 0;
        }
    }

    /**
     * Récupère le dernier ID d'editeur dans la base de données
     * @return Le dernier ID d'editeur ou 0 si aucun auteur n'existe
     * @throws SQLException Si erreur SQL
     */
    public int getDernierIDEditeur() throws SQLException {
        st = connexion.createStatement();
        r = st.executeQuery("SELECT MAX(idedit) AS dernierID FROM EDITEUR");
        if (r.next()) {
            return r.getInt("dernierID");
        } else {
            return 0;
        }
    }

    /**
     * Récupère le dernier ID classification dans la base de données
     * @return Le dernier ID classification ou 0 si aucun auteur n'existe
     * @throws SQLException Si erreur SQL
     */
    public int getDernierIDClassification() throws SQLException {
        st = connexion.createStatement();
        r = st.executeQuery("SELECT MAX(iddewey) AS dernierID FROM CLASSIFICATION");
        if (r.next()) {
            return r.getInt("dernierID");
        } else {
            return 0;
        }
    }
}
