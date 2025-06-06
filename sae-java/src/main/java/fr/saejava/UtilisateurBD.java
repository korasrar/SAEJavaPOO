package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UtilisateurBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;

    public UtilisateurBD(ConnexionMySQL connexion) {
        this.connexion = connexion;
    }

    /**
     * récupère un utilisateur à partir de son nom d'utilisateur et mot de passe
     *
     * @param username le nom d'utilisateur
     * @param mdp le mot de passe
     * @return l'utilisateur en question soit un Admin, Vendeur ou Client
     * @throws SQLException si erreur SQL 
     * @throws UtilisateurIntrouvableException si l'utilisateur n'est pas trouvé
     * @throws VendeurSansMagasinException si le vendeur n'a pas de magasin associé
     * @throws MotDePasseIncorrectException si le mot de passe est incorrect
     */
    public Utilisateur getUtilisateur(String username, String mdp) throws SQLException, UtilisateurIntrouvableException, VendeurSansMagasinException, MotDePasseIncorrectException {
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM UTILISATEUR NATURAL LEFT JOIN VENDEUR NATURAL LEFT JOIN ADMIN NATURAL LEFT JOIN CLIENT WHERE username = '" + username + "'");
        if (r.next()) {
            String nom = r.getString("nom");
            String prenom = r.getString("prenom");
            String motDePasse = r.getString("mdp");
            Role role = Role.valueOf(r.getString("role"));
            if(motDePasse.equals(mdp)){
            switch (role) {
                case ADMIN:
                    int idAdmin = r.getInt("idAdmin");
                    return new Admin(idAdmin, nom, prenom, username, motDePasse);
                case VENDEUR:
                    int idVendeur = r.getInt("idVendeur");
                    VendeurBD vendeurBD = new VendeurBD(connexion);
                    return new Vendeur(idVendeur, nom, prenom, username, motDePasse, vendeurBD.getMagasin(idVendeur));
                case CLIENT:
                    int idCli = r.getInt("idcli");
                    return new Client(idCli, motDePasse, motDePasse, r.getInt("codepostal"), nom, prenom, username, motDePasse);
                default:return null;
            }
            } else{
                throw new MotDePasseIncorrectException();
            }
        } else {
            throw new UtilisateurIntrouvableException();
        }
    }
}
