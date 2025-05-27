package fr.saejava;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class VendeurBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;
    MagasinBD magasinBD;

    public VendeurBD(ConnexionMySQL connexion){
        this.connexion = connexion;
        this.magasinBD = new MagasinBD(connexion);
    }

    public void ajouteLivre(Livre l) throws SQLException{ //execption a corriger
        magasinBD.ajoutStock(magasin, l, 1);
        // Appelle ajouteStock de magasin et qte = 1 par default
    }

    public void ajouteLivre(Livre l, int qte) throws SQLException{ //execption a corriger
        magasinBD.ajoutStock(magasin, l, qte);
        // Appelle ajouteStock de magasin
    }

    public void commander(List<Livre> livres, int qte, Client client){
        //Commande commande = new Commande(client, this.magasin);
        //for (Livre l : livres){
        //    try{
        //    if (this.verifierDispo(l)){ // verifie la dispo dans le magasin du vendeur
        //        commande.ajouterLivre(new DetailCommande(qte, l, commande));
        //        client.getHistoriquLivres().add(l);
        //    }
        //    }
        //    catch(LivrePasDansStockMagasinExecption e){
        //        System.out.println("Le livre n'est pas disponible dans le magasin");
        //    }
        //}

        // Ajoute une commande dans la table commande avec l'id du client mis paramètre
    }

    public void mettreAJour(Livre l, int qte) throws LivrePasDansStockMagasinExecption{
        // Vérifie si le livre est présent dans le stock avec un appelle de estDispo() et si true alors on modifie stock magasin, sinon on fait rien
    }

    public void transferer(Livre l, Magasin magasin2){
        // Supprime le livre du stock du magasin actuelle ou si il en reste plus de 1 enlever 1 de stock ? (co méthode) et le rajouter dans le stock de l'autre magasin
    }

    public boolean verifierDispo(Magasin magasin, Livre l) throws SQLException{
        st = connexion.createStatement();
        r = st.executeQuery("SELECT * FROM POSSEDER WHERE isbn = "+l.getIsbn()+" AND idmag = "+magasin.getId());
        if (r.next()){
            int qte = r.getInt("qte");
            // seconde verif de si sqt pas 0 ? vérifier dans une autre méthode la qte a 0 = sup le livre ou garder dans catalogue quand même ?
            if (qte > 0){return true;}
            else {return false;}
            }
        else {return false;}
        // Vérifier si un livre est présent dans stockmagasin d'un magasin (récup id ?)
    }

    public Magasin
}
