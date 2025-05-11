package fr.saejava;

public class Admin extends Personne{

    public Admin(String nom, String prenom){
        super(nom, prenom);
    }

    public void creeCompteVendeur(Magasin magasin, String nom, String prenom){
        Vendeur vendeur = new Vendeur(nom, prenom, magasin);
    }

    public void ajouterNouvelleLibrairie(){
        Magasin magasin = new Magasin();
    }

    public void gererLesStocks(){

    }

    public void statVente(){
        
    }
}