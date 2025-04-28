package fr.saejava;
import java.util.List;
import java.util.ArrayList;

public class StockMagasin {
    private int qte;
    private Livre livre;

    public StockMagasin(Livre livre,int qte){
        this.livre = livre;
        this.qte = qte;
    }

    public Livre getLivre(){
        return this.livre;
    }
    public int getQte(){
        return this.qte;
    }
    public void setLivre(Livre livre){
        this.livre=livre;
    }
    public void setQte(int qte){
        this.qte=qte;
    }
    
}
