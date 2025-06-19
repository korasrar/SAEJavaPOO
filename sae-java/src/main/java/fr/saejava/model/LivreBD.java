package fr.saejava.model;

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
    Livre livreSelectionner;

    public LivreBD(ConnexionMySQL connexion) {
        this.connexion = connexion;
    }

    public Map<Livre,Boolean> rechercherLivre(Filtre filtre, String isbn, String titre, String auteur, VendeurBD connexionVendeur) throws SQLException {
        Map<Livre,Boolean> livres = new HashMap<>();
        String stringBuilder = "SELECT * FROM LIVRE ";
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
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
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
        return livres;
    }

    public Map<Livre,Boolean> rechercherLivre(String isbn, String titre, String auteur, VendeurBD connexionVendeur, Vendeur vendeurConnecter) throws SQLException {
        Map<Livre,Boolean> livres = new HashMap<>();
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            try{
                r = st.executeQuery("SELECT * FROM MAGASIN NATURAL JOIN LIVRE NATURAL JOIN ECRIRE NATURAL JOIN AUTEUR WHERE nomauteur LIKE '%" + auteur + "%' OR levenshtein('"+auteur+"', nomauteur) between 0 and 4 and isbn='"+isbn+"' AND titre LIKE '%" + titre + "%' OR levenshtein('"+titre+"', titre) between 0 and 4 AND idmag!='"+connexionVendeur.getMagasin(vendeurConnecter.getIdVendeur())+"'");
            } catch (VendeurSansMagasinException e){
                return new HashMap<>();
            }
            while(r.next()){
                Livre livre = new Livre(r.getString("isbn"), r.getString("titre"), r.getInt("nbpages"), r.getString("datepubli"), r.getDouble("prix"));
                livres.put(livre, connexionVendeur.verifierDispo(livre));
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
        return livres;
    }

    public List<Auteur> rechercheAuteur(String nomAuteur) throws SQLException{
        List<Auteur> auteurs = new ArrayList<>();
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT * FROM AUTEUR WHERE nomauteur LIKE '%"+ nomAuteur +"%' OR levenshtein('" + nomAuteur + "', nomauteur) between 0 and 2");
            while (r.next()) {
                auteurs.add(new Auteur(r.getInt("idauteur"), r.getString("nomauteur")));
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
        if (auteurs.isEmpty()) {
            return null;
        } else {
            return auteurs;
        }
    }

    public List<Editeur> rechercheEditeur(String nomEditeur) throws SQLException {
        List<Editeur> editeurs = new ArrayList<>();
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT * FROM EDITEUR WHERE nomedit LIKE '%" +nomEditeur + "%' OR levenshtein('" + nomEditeur + "', nomedit) between 0 and 2");
            while (r.next()) {
                editeurs.add(new Editeur(r.getInt("idedit"), r.getString("nomedit")));
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
        if (editeurs.isEmpty()) {
            return null;
        } else {
            return editeurs;
        }
    }

    public List<Classification> rechercheClassification(String nomClassification) throws SQLException {
        List<Classification> classifications = new ArrayList<>();
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT * FROM CLASSIFICATION WHERE nomclass LIKE '%" + nomClassification + "%' OR levenshtein('" + nomClassification + "', nomclass) between 0 and 2");
            while (r.next()) {
                classifications.add(new Classification(r.getInt("iddewey"), r.getString("nomclass")));
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
        if (classifications.isEmpty()) {
            return null;
        } else {
            return classifications;
        }
    }

    public void insererNouvelAuteur(Auteur auteur) throws SQLException {
        PreparedStatement pstmt = connexion.prepareStatement("SELECT * FROM AUTEUR WHERE nomauteur = ? and idauteur IS NOT NULL");
        ResultSet r = null;
        try {
            pstmt.setString(1, auteur.getNomAuteur());
            r = pstmt.executeQuery();
            if (r.next()) {
                throw new SQLException("L'auteur existe déjà");
            } else {
                PreparedStatement pstmt2 = connexion.prepareStatement("INSERT INTO AUTEUR (nomauteur) VALUES (?)");
                pstmt2.setString(1, auteur.getNomAuteur());
                pstmt2.executeUpdate();
                pstmt2.close();
            }
        } finally {
            if (r != null) r.close();
            if (pstmt != null) pstmt.close();
        }
    }

    public void insererNouvelEditeur(Editeur editeur) throws SQLException {
        PreparedStatement pstmt = connexion.prepareStatement("SELECT * FROM EDITEUR WHERE nomedit = ?");
        ResultSet r = null;
        try {
            pstmt.setString(1, editeur.getNomEdit());
            r = pstmt.executeQuery();
            if (r.next()) {
                throw new SQLException("L'éditeur existe déjà");
            } else {
                PreparedStatement pstmt2 = connexion.prepareStatement("INSERT INTO EDITEUR (nomedit) VALUES (?)");
                pstmt2.setString(1, editeur.getNomEdit());
                pstmt2.executeUpdate();
                pstmt2.close();
            }
        } finally {
            if (r != null) r.close();
            if (pstmt != null) pstmt.close();
        }
    }

    public void insererNouvelleClassification(Classification classification) throws SQLException {
        PreparedStatement pstmt = connexion.prepareStatement("SELECT * FROM CLASSIFICATION WHERE nomclass = ?");
        ResultSet r = null;
        try {
            pstmt.setString(1, classification.getNomClass());
            r = pstmt.executeQuery();
            if (r.next()) {
                throw new SQLException("La classification existe déjà");
            } else {
                PreparedStatement pstmt2 = connexion.prepareStatement("INSERT INTO CLASSIFICATION (nomclass) VALUES (?)");
                pstmt2.setString(1, classification.getNomClass());
                pstmt2.executeUpdate();
                pstmt2.close();
            }
        } finally {
            if (r != null) r.close();
            if (pstmt != null) pstmt.close();
        }
    }

    public int getDernierIDAuteur() throws SQLException {
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT MAX(idauteur) AS dernierID FROM AUTEUR");
            if (r.next()) {
                return r.getInt("dernierID");
            } else {
                return 0;
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
    }

    public int getDernierIDEditeur() throws SQLException {
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT MAX(idedit) AS dernierID FROM EDITEUR");
            if (r.next()) {
                return r.getInt("dernierID");
            } else {
                return 0;
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
    }

    public int getDernierIDClassification() throws SQLException {
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT MAX(iddewey) AS dernierID FROM CLASSIFICATION");
            if (r.next()) {
                return r.getInt("dernierID");
            } else {
                return 0;
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
    }

    public Livre getLivreSelectionner() {
        return livreSelectionner;
    }
    
    public void setLivreSelectionner(Livre livreSelectionner) {
        this.livreSelectionner = livreSelectionner;
    }
}
