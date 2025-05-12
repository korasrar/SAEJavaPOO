package fr.saejava;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class Client extends Personne{
    
    private int num;
    private String adresse;
    private String ville;
    private int codePostal;
    private Set<Livre> historiqueLivre;
    private List<Commande> historiqueCommandes;

    public Client(String adresse, String ville, int codePostal, String nom, String prenom){
        super(nom, prenom);
        this.num++;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
        this.historiqueCommandes = new ArrayList<>();
        this.historiqueLivre=new HashSet<>();
    }
    public int getNum(){ 
        return this.num;
    }
    public String getAdresse(){
        return this.adresse;
    }
    public String getVille(){
        return this.ville;
    }
    public int getCodePostal(){
        return this.codePostal;
    }
    public List<Commande> getHistoriqueCommandes(){
        return this.historiqueCommandes;
    }
    public Set<Livre> getHistoriquLivres(){
        return this.historiqueLivre;
    }
    public void setNum(int num){ 
        this.num = num;
    }
    public void setAdresse(String adresse){
        this.adresse = adresse;
    }
    public void setVille(String ville){
        this.ville = ville;
    }
    public void setCodePostal(int codePostal){
        this.codePostal = codePostal;
    }

    public void passerCommande(){
        // this.historiqueLivre.addAll(Livre);
        Commande commande = new Commande(this, null);
        Scanner scan = new Scanner(System.in);
        System.out.println("Choisir magasin parmis la liste :\n");
        System.out.println();
        String magasinSelec = scan.nextLine(); // Retrouver magasin a partir de string 
        // Liste de magasin ou ?
        commande.setMagasin(magasinSelec);
        scan.close();
    }
    public void modeDeReception(){
        

    }

    public void consulterCatalogue(){
        
    }
    
    public Set<Livre> onVousRecommande(Client client){
        // liste de tt les clients du magasin?, faire une copi de la liste et enlever le client en paramettre

        // recuperer la liste de commandes de chaques client de la liste et du client passe en paramettre

        // comparer les listes

        // faire une liste avec les livres qui ne sont pas en commun pour chaque client qui a au moins un 
        // livre en commun avec la liste du client passe en paramettre   
        return null;
    }
}