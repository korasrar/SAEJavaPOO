package fr.saejava.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UtilisateurBD {
    ConnexionMySQL connexion;
    Utilisateur utilisateurConnecter;

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
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT * FROM UTILISATEUR u LEFT JOIN VENDEUR v ON u.idutilisateur = v.idvendeur LEFT JOIN ADMIN a ON u.idutilisateur = a.idadmin LEFT JOIN CLIENT c ON u.idutilisateur = c.idcli WHERE u.username = '" + username + "'");
            if (r.next()) {
                String nom = r.getString("nom");
                String prenom = r.getString("prenom");
                String motDePasse = r.getString("motdepasse");
                Role role = Role.valueOf(r.getString("role"));
                if(motDePasse.equals(mdp)){
                    switch (role) {
                        case admin:
                            int idAdmin = r.getInt("idadmin");
                            return new Admin(idAdmin, nom, prenom, username, motDePasse);
                        case vendeur:
                            int idVendeur = r.getInt("idvendeur");
                            VendeurBD vendeurBD = new VendeurBD(connexion);
                            return new Vendeur(idVendeur, nom, prenom, username, motDePasse, vendeurBD.getMagasin(idVendeur));
                        case client:
                            int idCli = r.getInt("idcli");
                            String adresse = r.getString("adressecli");
                            String ville = r.getString("villecli");
                            int codePostal = r.getInt("codepostal");
                            return new Client(idCli, adresse, ville, codePostal, nom, prenom, username, motDePasse);
                        default:
                            return null;
                    }
                } else{
                    throw new MotDePasseIncorrectException();
                }
            } else {
                throw new UtilisateurIntrouvableException();
            }
        } finally {
            if (r != null) r.close();
            if (st != null) st.close();
        }
    }

    public int getDernierID() throws SQLException {
        Statement st = connexion.createStatement();
        ResultSet r = null;
        try {
            r = st.executeQuery("SELECT MAX(idutilisateur) AS dernierID FROM UTILISATEUR");
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

    public void setUtilisateurConnecter(Utilisateur util){
        this.utilisateurConnecter=util;
    }

    public Utilisateur getUtilisateurConnecter(){
        return utilisateurConnecter;
    }

    public void deconnecter() {
        this.utilisateurConnecter = null;
    }
}
