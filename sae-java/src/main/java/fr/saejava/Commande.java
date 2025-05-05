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

    void setClient(Client client) {
        this.client = client;
    }
    void setMagasin(Magasin magasin) {
        this.magasin = magasin;
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
    @Override
    public boolean equals(Object obj){
        if(obj==null){return false;}
        if(obj==this){return true;}
        if(!(obj instanceof Commande)){return false;}
        Commande commande=(Commande) obj;
        return this.client.equals(commande.getClient()) && this.magasin.equals(commande.getMagasin()) && this.contenue.equals(commande.getContenue());
    }

}