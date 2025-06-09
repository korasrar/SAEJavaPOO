package fr.saejava;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Commande {
    // Map<Livre, Integer> contenu; peut etre ?
    int numcom;
    Date dateCom;
    List<DetailCommande> contenue;
    Client client;
    Magasin magasin;

    public Commande(int numcom, Date dateCom, Client client,Magasin magasin){
        this.numcom=numcom;
        this.contenue=new ArrayList<>();
        this.client=client;
        this.magasin=magasin;
        this.dateCom=dateCom;
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

    public  Date getDateCommande(){
        return this.dateCom;
    }

    void setClient(Client client) {
        this.client = client;
    }
    void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }
    public void setDateCommande(Date dateCommande) {
        this.dateCom = dateCommande;
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
        return this.numcom==commande.numcom && this.client.equals(commande.getClient()) && this.magasin.equals(commande.getMagasin()) && this.contenue.equals(commande.getContenue());
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(numcom) + client.hashCode() + magasin.hashCode() + contenue.hashCode();
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