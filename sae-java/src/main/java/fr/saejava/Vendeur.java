package fr.saejava;
import java.util.*;

public class Vendeur extends Personne{
    
    private int idVendeur;
    private Magasin magasin;

    public Vendeur(String nom, String prenom, Magasin magasin){
        super(nom, prenom);
        this.idVendeur++;
        this.magasin = magasin;
    }

    public void ajouteLivre(Livre l){
        if (magasin.getStock().contains(l)){
            magasin.getStock().get(magasin.getStock().indexOf(l)).ajouteQte();
        }
        else{
            StockMagasin stock = new StockMagasin(l, 1);
        }
    }

    public void ajouteLivre(Livre l, int qte){
        if (magasin.getStock().contains(l)){
            magasin.getStock().get(magasin.getStock().indexOf(l)).ajouteQte(qte);}
        else{
            magasin.ajoutStock(new StockMagasin(l, 1));}
    }

    public void commander(List<Livre> livres, int qte, Client client){
        Commande commande = new Commande(client, this.magasin);
        for (Livre l : livres){
            try{
            if (this.verifierDispo(l)){ // verifie la dispo dans le magasin du vendeur
                commande.ajouterLivre(new DetailCommande(qte, l, commande));
            }
            }
            catch(LivrePasDansStockMagasinExecption e){
                System.out.println("Le livre n'est pas disponible dans le magasin");
            }
        }
    }

    public void commander(List<Livre> livres, Client client){ // qte 1 ? ou tout dans la meme methode
        Commande commande = new Commande(client, this.magasin);
        for (Livre l : livres){
            try{
            if (this.verifierDispo(l)){ // verifie la dispo dans le magasin du vendeur
                commande.ajouterLivre(new DetailCommande(l, commande));
            }
            }
            catch(LivrePasDansStockMagasinExecption e){
                System.out.println("Le livre n'est pas disponible dans le magasin");
            }
        }
    }

    public void mettreAJour(Livre l, int qte) throws LivrePasDansStockMagasinExecption{
        if (magasin.getStock().contains(l)){
            magasin.getStock().get(magasin.getStock().indexOf(l)).setQte(qte);}
        else {
            throw new LivrePasDansStockMagasinExecption();}
    }

    public void transferer(StockMagasin stock){

    }

    public boolean verifierDispo(Livre l) throws LivrePasDansStockMagasinExecption{

        if (this.magasin.getStock().contains(l))
        {return true;}

        else {
        throw new LivrePasDansStockMagasinExecption();
        }
    }

}
