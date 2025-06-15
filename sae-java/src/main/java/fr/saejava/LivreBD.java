package fr.saejava;

import java.sql.PreparedStatement;
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
        r.close();
        st.close();
        return livres;
    }

    //public Livre rechercherLivres(String isbn, String titre, String auteur, VendeurBD connexionVendeur) throws SQLException {
    //    st = connexion.createStatement();
    //    r = st.executeQuery("SELECT * FROM LIVRE WHERE isbn='"+isbn+"' AND titre='"+titre+"' AND auteur'"+auteur+"'");
    //    Livre livre = new Livre(r.getString("isbn"), r.getString("titre"), r.getInt("nbpages"), r.getString("datepubli"), r.getDouble("prix"));
    //    return livre;
    //}

    public Map<Livre,Boolean> rechercherLivre(String isbn, String titre, String auteur, VendeurBD connexionVendeur, Vendeur vendeurConnecter) throws SQLException {
        st = connexion.createStatement();
        Map<Livre,Boolean> livres = new HashMap<>();
        try{
        r = st.executeQuery("SELECT * FROM MAGASIN NATURAL JOIN LIVRE NATURAL JOIN ECRIRE NATURAL JOIN AUTEUR WHERE nomauteur LIKE '%" + auteur + "%' OR levenshtein('"+auteur+"', nomauteur) between 0 and 4 and isbn='"+isbn+"' AND titre LIKE '%" + titre + "%' OR levenshtein('"+titre+"', titre) between 0 and 4 AND idmag!='"+connexionVendeur.getMagasin(vendeurConnecter.getIdVendeur())+"'");
        } catch (VendeurSansMagasinException e){
            return new HashMap<>();
        }
        while(r.next()){
            Livre livre = new Livre(r.getString("isbn"), r.getString("titre"), r.getInt("nbpages"), r.getString("datepubli"), r.getDouble("prix"));
            livres.put(livre, connexionVendeur.verifierDispo(livre));
        }
        r.close();
        st.close();
        return livres;
    }
}
