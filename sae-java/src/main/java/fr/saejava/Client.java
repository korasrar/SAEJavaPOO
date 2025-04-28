package fr.saejava;

public class Client extends Personne{
    private int num;
    private String adresse;
    private String ville;
    private int codePostal;
    public Client(int num, String adresse, String ville, int codePostal, String nom, String prenom){
        super(nom, prenom);
        this.num = num;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
    }
    public void passerCommande(){

    }
    public void modeDeReception(){

    }
    public void consulterCatalogue(){
        
    }
}