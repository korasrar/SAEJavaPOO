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
                    r.close();
                    st.close();
                    return new Admin(idAdmin, nom, prenom, username, motDePasse);
                case vendeur:
                    int idVendeur = r.getInt("idvendeur");
                    r.close();
                    st.close();
                    VendeurBD vendeurBD = new VendeurBD(connexion);
                    return new Vendeur(idVendeur, nom, prenom, username, motDePasse, vendeurBD.getMagasin(idVendeur));
                case client:
                    int idCli = r.getInt("idcli");
                    String adresse = r.getString("adressecli");
                    int codePostal = r.getInt("codepostal");
                    r.close();
                    st.close();
                    return new Client(idCli, adresse, motDePasse, codePostal, nom, prenom, username, motDePasse);
                default:
                    r.close();
                    st.close();
                    return null;
            }
            } else{
                r.close();
                st.close();
                throw new MotDePasseIncorrectException();
            }
        } else {
            r.close();
            st.close();
            throw new UtilisateurIntrouvableException();
        }
    }

    public int getLastUtilisateurID() throws SQLException{
        int nb=0;
		st=connexion.createStatement();
		ResultSet rs=st.executeQuery("select max(idutilisateur) as nb from UTILISATEUR");

        while(rs.next()){
			nb=rs.getInt("nb");
			System.out.println(nb);
		}
			rs.close();
		return nb;
    }
}
