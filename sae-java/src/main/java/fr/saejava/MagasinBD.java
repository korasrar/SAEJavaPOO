package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MagasinBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet rs;

    public MagasinBD(ConnexionMySQL connexion){
        this.connexion = connexion;
    }

    /**
     * permet de consulter le catalogue de livres d'un magasin
     * @param magasin le magasin dont on veut consulter le catalogue
     * @return un ensemble contenant les livres du catalogue
     * @throws SQLException si une erreur SQL se produit
     */

    public void ajoutStock(Magasin magasin, Livre l, Integer qte) throws SQLException{ 
        st = connexion.createStatement();
        rs = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"' AND idmag = '"+magasin.getId()+"'");
        System.out.println(rs);
        /*if(rs.next()){
            int qteExistante = rs.getInt("qte");
            int nouvelleQte = qteExistante+qte;
            st.executeUpdate("UPDATE POSSEDER SET qte ="+nouvelleQte+" WHERE isbn = "+l.getIsbn()+" AND igmag = "+magasin.getId());
        } 
        else {
            st.executeUpdate("INSERT INTO POSSEDER (isbn, idmag, qte) VALUES ("+l.getIsbn()+", " + magasin.getId()+", "+qte+")");
        }*/
        // Requete d'insert de stock, vérifier si le livre est deja dans la bd, sinon le rajouter, si oui modifier la table stock magasin
    }
}





/* Regarder si les éditeurs du livres sont dans la base, idem pour les classification et les auteurs (boucle for)





*/
/* 
    public void ajoutStock(Magasin magasin, Livre l, Integer qte) throws SQLException{
        st = connexion.createStatement();
        rs = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = '"+l.getIsbn()+"' AND idmag = '"+magasin.getId()+"'");

    }

    		PreparedStatement pstmt = laConnexion.prepareStatement("INSERT INTO JOUEUR VALUES ("+(maxNumJoueur()+1)+",'"+j.getPseudo()+"','"+j.getMotdepasse()+"','"+value+"','"+j.getMain()+"',"+j.getNiveau()+")");
		pstmt.executeUpdate();
*/
