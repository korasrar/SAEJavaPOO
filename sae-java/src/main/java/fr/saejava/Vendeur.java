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

    public void commander(){
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

        if (this.magasin.getStock().contains(l)){
        {return true;}

        else {
        throw new LivrePasDansStockMagasinExecption();
        return false;}
    }

}
}