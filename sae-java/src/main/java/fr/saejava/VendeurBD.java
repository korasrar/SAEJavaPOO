package fr.saejava;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class VendeurBD {
    ConnexionMySQL connexion;
    Statement st;
    ResultSet r;

    public VendeurBD(ConnexionMySQL connexion){
        this.connexion = connexion;
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

    public void transferer(Magasin magasin2){
        // Supprime le livre du stock du magasin actuelle ou si il en reste plus de 1 enlever 1 de stock ? (co méthode) et le rajouter dans le stock de l'autre magasin
    }

    public boolean verifierDispo(Livre l) throws LivrePasDansStockMagasinExecption{
        // Vérifier si un livre est présent dans stockmagasin d'un magasin (récup id ?)
    }
}
