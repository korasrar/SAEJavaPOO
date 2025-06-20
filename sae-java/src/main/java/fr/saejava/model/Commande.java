package fr.saejava.model;

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
    ModeLivraison modeLivraison;

    public Commande(int numcom, Date dateCom, Client client,Magasin magasin, ModeLivraison modeLivraison){
        this.numcom=numcom;
        this.contenue=new ArrayList<>();
        this.client=client;
        this.magasin=magasin;
        this.dateCom=dateCom;
        this.modeLivraison=modeLivraison;
    }

    public Client getClient(){
        return this.client;
    }

    public Magasin getMagasin(){
        return this.magasin;
    }

    public List<DetailCommande> getContenue(){
        return this.contenue;
    }

    public int getNumcom(){
        return this.numcom;
    }

    public Date getDateCommande(){
        return this.dateCom;
    }

    public ModeLivraison getModeLivraison() {
        return this.modeLivraison;
    }

    public void setNumcom(int numcom) {
        this.numcom = numcom;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }
    public void setDateCommande(Date dateCommande) {
        this.dateCom = dateCommande;
    }
    public void setModeLivraison(ModeLivraison modeLivraison) {
        this.modeLivraison = modeLivraison;
    }

    public void ajouterDetailCommande(DetailCommande ajout){
        this.contenue.add(ajout);
    }

    @Override
    public String toString(){
        return "Commande :" +
                "numcom=" + numcom +
                ", dateCom=" + dateCom +
                ", magasin=" + magasin +
                ", modeLivraison=" + modeLivraison;
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

    // *----------------------------* //

    /**
     * vérifie si la commande contient un livre spécifique
     * @param livre le livre à vérifier
     * @return true si le livre est dans la commande, false sinon
     */
    public boolean contientLivre(Livre livre){
        for(DetailCommande detail : this.contenue){
            if(detail.getLivre().equals(livre)){
                return true;
            }
        }
        return false;
    }

    public DetailCommande livreDansCommande(Livre livre){
        for(DetailCommande detail : this.contenue){
            if(detail.getLivre().equals(livre)){
                return detail;
            }
        }
        return null;
    }

    public double prixTotal(){
        double total=0;
        for(DetailCommande detail : this.contenue){
            total+=detail.getPrixVente();
        }
        return total;
    }

    /**
     * ajoute un livre au panier
     * @param livre
     */
    void commander(Livre livre, int qte){
        if(this.contientLivre(livre)){
            DetailCommande detail=this.livreDansCommande(livre);
            detail.setQte(detail.getQte()+1);
        }
        else{
            this.ajouterDetailCommande(new DetailCommande(qte,livre,this));
        }
    }

    public void supprimerDetail(DetailCommande detail) {
        this.contenue.remove(detail);
    }

    public void viderCommande() {
        this.contenue.clear();
    }
}