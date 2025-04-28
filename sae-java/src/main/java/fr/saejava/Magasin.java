package fr.saejava;
import java.util.List;
import java.util.ArrayList;

public class Magasin{
    private int id;
    private String nom;
    private String ville;
    private List<StockMagasin> stock; //liste de livre?

    public Magasin(int id,String nom,String ville){
        this.id = id;
        this.nom = nom;
        this.ville = ville;
        this.stock = new ArrayList<>();
    }

    public int getId(){
        return this.id;
    }
    public String getNom(){
        return this.nom;
    }
    public String getVille(){
        return this.ville;
    }
    public List<StockMagasin> getStock(){
        return this.stock;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setNom(String nom){
        this.nom = nom;
    }
    public void setVille(String ville){
        this.ville = ville;
    }


    public void ajoutStock(StockMagasin ajout){
        this.stock.add(ajout);
    }
    /*====================================*/
    public Livre onVousRecommande(){
        return ;
    }
    public int getQte(int i){
        return stock.indexOf(i);
    }
    public int getQte(Livre livre){
        for(StockMagasin article:this.stock){
            if (article.getLivre().equals(livre)){
                return article.getQte();
            }
        }
        return 0;
    }
}