package fr.saejava;

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
}
