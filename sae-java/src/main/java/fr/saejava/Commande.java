package fr.saejava;
import java.util.ArrayList;

public class Commande {
    ArrayList<DetailCommande> contenue;
    Client client;
    Magasin magasin;

    public Commande(Client client,Magasin magasin){
        this.contenue=new ArrayList<>();
        this.client=client;
        this.magasin=magasin;
    }
    Client getClient(){
        return this.client;
    }
    Magasin getMagasin(){
        return this.magasin;
    }
    ArrayList<DetailCommande> getContenue(){
        return this.contenue;
    }

    void ajoutLivre(DetailCommande ajout){
        this.contenue.add(ajout);
    }

    @Override
    public String toString(){
        String res=" ";
        for(DetailCommande article:this.contenue){
            res+=article;
        }
        return res;
    }

}