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
    public void modeDeReception(){
        

    }
}