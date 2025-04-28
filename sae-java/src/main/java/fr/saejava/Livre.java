package fr.saejava;
import java.util.List;
import java.util.ArrayList;

public class Livre {
    private int isbn;
    private String titre;
    private int nbPages;
    private String datePubli;
    private double prix;
    private List<Auteur> lesAuteurs;
    private List<Classification> lesClassifications;
    private List<Editeur> lesEditeurs;
    //private List<StockMagasin> lesMagasin;
    //private List<DetailCommande> lesCommandes;

    public Livre(int isbn, String titre, int nbPages, String datePubli, double prix) {
        this.isbn = isbn;
        this.titre = titre;
        this.nbPages = nbPages;
        this.datePubli = datePubli;
        this.prix = prix;
        this.lesAuteurs = new ArrayList<>();
        this.lesClassifications = new ArrayList<>();
        this.lesEditeurs = new ArrayList<>();

    }

    public int getIsbn(){
        return this.isbn;
    }
    public String getTitre(){
        return this.titre;
    }
    public int getNbPages(){
        return this.nbPages;
    }
    public String getDatePubli(){
        return this.datePubli;
    }
    public double getPrix(){
        return this.prix;
    }
    public List<Auteur> getAuteurs(){
        return this.lesAuteurs;
    }
    public List<Classification> getClassifications(){
        return this.lesClassifications;
    }
    public List<Editeur> getEditeurs(){
        return this.lesEditeurs;
    }

    public void setIsbn(int isbn){
        this.isbn = isbn;
    }
    public void setTitre(String titre){
        this.titre = titre;
    }
    public void setNbPages(int nbPages){
        this.nbPages = nbPages;
    }
    public void setDatePubli(String datePubli){
        this.datePubli = datePubli;
    }
    public void setPrix(double prix){
        this.prix = prix;
    }
    
}

