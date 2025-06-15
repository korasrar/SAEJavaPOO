package fr.saejava;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Magasin{
    private int id;
    private String nom;
    private String ville;
    private Map<Livre, Integer> stockMagasin;
    private List<Vendeur> vendeurs;
    

    public Magasin(int id,String nom,String ville){
        this.id = id;
        this.nom = nom;
        this.ville = ville;
        this.stockMagasin = new HashMap<>();
        this.vendeurs = new ArrayList<>();
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
    public Map<Livre, Integer> getStock(){
        return this.stockMagasin;
    }
    public List<Vendeur> getVendeurs(){
        return this.vendeurs;
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
    
    public void addVendeur(Vendeur vendeur){
        if(!vendeurs.contains(vendeur)){
            this.vendeurs.add(vendeur);
        }
    }

    @Override
    public boolean equals(Object obj){
        if(obj==null){return false;}
        if(obj==this){return true;}
        if(!(obj instanceof Magasin)){return false;}
        Magasin magasin=(Magasin) obj;
        return this.id==magasin.getId();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString(){
        return this.nom + " (" + this.ville + ")";
    }

    /*====================================*/
    
}