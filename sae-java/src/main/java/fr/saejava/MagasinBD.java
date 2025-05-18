package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MagasinBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;

    public MagasinBD(ConnexionMySQL connexion){
        this.connexion = connexion;
    }

    public void ajoutStock(Magasin magasin, Livre l, Integer qte) throws SQLException{
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = "+l.getIsbn()+" AND idmag = "+magasin.getId());
        if(r.next()){
            int qteExistante = r.getInt("qte");
            int nouvelleQte = qteExistante+qte;
            st.executeUpdate("UPDATE POSSEDER SET qte ="+nouvelleQte+" WHERE isbn = "+l.getIsbn()+" AND igmag = "+magasin.getId());
        } 
        else {
            st.executeUpdate("INSERT INTO POSSEDER (isbn, idmag, qte) VALUES ("+l.getIsbn()+", " + magasin.getId()+", "+qte+")");
        }
        // Requete d'insert de stock, vérifier si le livre est deja dans la bd, sinon le rajouter, si oui modifier la table stock magasin
    }
}
