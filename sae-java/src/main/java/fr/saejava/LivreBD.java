package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
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
                stringBuilder += "NATURAL JOIN ECRIRE NATURAL JOIN AUTEUR WHERE nomauteur LIKE '%" + auteur + "%' OR levenshtein('"+auteur+"', nomauteur) between 0 and 4";
                break;
            case titre:
                stringBuilder += "WHERE titre LIKE '%" + titre + "%' OR levenshtein('"+titre+"', titre) between 0 and 4";
                break;
        }
        r = st.executeQuery(stringBuilder);
        while(r.next()){
            Livre livre = new Livre(r.getString("isbn"), r.getString("titre"), r.getInt("nbpages"), r.getString("datepubli"), r.getDouble("prix"));
            livres.put(livre, connexionVendeur.verifierDispo(livre));
        }
        return livres;
    }
}
