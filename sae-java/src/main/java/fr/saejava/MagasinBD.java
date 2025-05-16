package fr.saejava;

import java.sql.ResultSet;
import java.sql.Statement;

public class MagasinBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;

    public MagasinBD(ConnexionMySQL connexion){
        this.connexion = connexion;
    }

    public void ajoutStock(Livre l, Integer qte){
        // Requete d'insert de stock, vérifier si le livre est deja dans la bd, sinon le rajouter, si oui modifier la table stock magasin
    }
}
