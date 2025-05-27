package fr.saejava;

import java.sql.ResultSet;
import java.sql.Statement;

public class UtilisateurBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;

    public UtilisateurBD(ConnexionMySQL connexion) {
        this.connexion = connexion;
    }

    public Utilisateur getUtilisateur(String username) throws UtilisateurIntrouvableException {
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM UTILISATEUR NATURAL LEFT JOIN VENDEUR NATURAL LEFT JOIN ADMIN NATURAL LEFT JOIN CLIENT WHERE username = '" + username + "'");
        if (r.next()) {
            String nom = r.getString("nom");
            String prenom = r.getString("prenom");
            String motDePasse = r.getString("mdp");
            Role role = Role.valueOf(r.getString("role"));
            switch (role) {
                case ADMIN:
                    int idAdmin = r.getInt("idAdmin");
                    return new Admin(idAdmin, nom, prenom, username, motDePasse);
                case VENDEUR:
                    int idVendeur = r.getInt("idVendeur");
                    return new Vendeur(idVendeur, nom, prenom, username, motDePasse, );
                case CLIENT:
                    break;
            
                default:
                    break;
            }
            
        } else {
            throw new UtilisateurIntrouvableException();
        }
    }
}
