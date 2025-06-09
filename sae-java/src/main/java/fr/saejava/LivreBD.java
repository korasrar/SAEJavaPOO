package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class LivreBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;

    public LivreBD(ConnexionMySQL connexion) {
        this.connexion = connexion;
    }

    public Map<Livre,Boolean> getLivres(Filtre filtre, String isbn, String titre, String auteur) throws SQLException {
        st = connexion.createStatement();
        String stringBuilder = "SELECT * FROM livre ";
        
        switch (filtre) {
            case isbn:
                stringBuilder += "WHERE isbn='"+isbn+"'";
                break;
            case auteur:
                stringBuilder += "WHERE auteur LIKE '%" + auteur + "%'";
                break;
            case titre:
                stringBuilder += "WHERE titre LIKE '%" + titre + "%'";
                break;
        
        r = st.executeQuery(stringBuilder);
        while(r.next()){
            Livre livre = new Livre();
            livres.add(livre);
        }
        return livres;
    }
}
}
