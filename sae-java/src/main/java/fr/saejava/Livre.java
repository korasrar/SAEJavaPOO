package fr.saejava;
import java.util.List;
import java.util.ArrayList;

public class Livre {
    private String isbn;
    private String titre;
    private int nbPages;
    private String datePubli;
    private double prix;
    private List<Auteur> lesAuteurs; //
    private List<Classification> lesClassifications; //
    private List<Editeur> lesEditeurs; //

    public Livre(String isbn, String titre, int nbPages, String datePubli, double prix) {
        this.isbn = isbn;
        this.titre = titre;
        this.nbPages = nbPages;
        this.datePubli = datePubli;
        this.prix = prix;
        this.lesAuteurs = new ArrayList<>();
        this.lesClassifications = new ArrayList<>();
        this.lesEditeurs = new ArrayList<>();

    }

    public String getIsbn(){
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

    public void setIsbn(String isbn){
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

    // *---------------------------------------* //
    
    @Override
    public boolean equals(Object obj){
        if(obj==null){return false;}
        if(obj==this){return true;}
        if(!(obj instanceof Livre)){return false;}

        Livre tmp = (Livre) obj;
        return tmp.isbn.equals(this.isbn);
    }

    @Override
    public int hashCode() {
        return String.valueOf(isbn).hashCode();
    }

    @Override
    public String toString() {
        return this.titre + " (" + this.isbn + ") - " + this.nbPages + " pages - " + this.datePubli + " - " + this.prix + "€"; 
    }

}

