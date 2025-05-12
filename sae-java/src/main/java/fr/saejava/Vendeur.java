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
        magasin.getStock().putIfAbsent(l,0);
        magasin.getStock().put(l,magasin.getStock().get(l)+1);
    }

    public void ajouteLivre(Livre l, int qte){
        magasin.getStock().putIfAbsent(l,0);
        magasin.getStock().put(l,magasin.getStock().get(l)+qte);
    }

    public void commander(List<Livre> livres, int qte, Client client){
        Commande commande = new Commande(client, this.magasin);
        for (Livre l : livres){
            try{
            if (this.verifierDispo(l)){ // verifie la dispo dans le magasin du vendeur
                commande.ajouterLivre(new DetailCommande(qte, l, commande));
                client.getHistoriquLivres().add(l);
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
                client.getHistoriquLivres().add(l);
            }
            }
            catch(LivrePasDansStockMagasinExecption e){
                System.out.println("Le livre n'est pas disponible dans le magasin");
            }
        }
    }

    public void mettreAJour(Livre l, int qte) throws LivrePasDansStockMagasinExecption{
        if (this.magasin.getStock().containsKey(l) && this.magasin.getStock().get(l)!=0){
            magasin.getStock().put(l,qte);}
        else {
            throw new LivrePasDansStockMagasinExecption();}
    }

    public void transferer(Magasin magasin2){

    }

    public boolean verifierDispo(Livre l) throws LivrePasDansStockMagasinExecption{

        if (this.magasin.getStock().containsKey(l) && this.magasin.getStock().get(l)!=0)
        {return true;}

        else {
        throw new LivrePasDansStockMagasinExecption();
        }
    }

}
