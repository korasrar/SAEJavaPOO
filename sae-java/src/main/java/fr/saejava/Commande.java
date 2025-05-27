package fr.saejava;

import java.util.ArrayList;
import java.util.List;

public class Commande {
    // Map<Livre, Integer> contenu; peut etre ?
    int numcom;
    List<DetailCommande> contenue;
    Client client;
    Magasin magasin;

    public Commande(int numcom, Client client,Magasin magasin){
        this.numcom=numcom;
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

    List<DetailCommande> getContenue(){
        return this.contenue;
    }

    int getNumcom(){
        return this.numcom;
    }

    void setClient(Client client) {
        this.client = client;
    }
    void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    void ajouterDetailCommande(DetailCommande ajout){
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
// faire hashcode

    // *----------------------------* //
    public boolean contientLivre(Livre livre){
        for(DetailCommande detail : this.contenue){
            if(detail.getLivre().equals(livre)){
                return true;
            }
        }
        return false;
    }
}