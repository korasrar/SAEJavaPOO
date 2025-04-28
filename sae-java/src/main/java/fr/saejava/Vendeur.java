package fr.saejava;

public class Vendeur extends Personne{
    
    private int idVendeur;

    public Vendeur(String nom, String prenom, int idVendeur){
        super(nom, prenom);
        this.idVendeur = idVendeur;
    }

    public void ajouteLivre(Livre l){

    }

    public void commander(){

    }

    public void mettreAJour(){

    }

    public void transferer(){

    }

    public boolean verifierDispo(){
        return false;
    }
}