package fr.saejava;

import java.sql.SQLException;

public class Vendeur extends Utilisateur{
    
    private int idVendeur;
    private Magasin magasin;

    public Vendeur(int idVendeur, String nom, String prenom,String username, String motDePasse, Magasin magasin){
        super(idVendeur, nom, prenom, username, motDePasse, Role.vendeur);
        this.idVendeur = idVendeur;
        this.magasin = magasin;
    }

    public int getIdVendeur() {
        return idVendeur;
    }

    public Magasin getMagasin() {
        return magasin;
    }

    public void setIdVendeur(int idVendeur) {
        this.idVendeur = idVendeur;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    // --------------------------- //

    @Override
    public boolean equals(Object obj){
        if(this == obj){return true;}
        if(obj == null){return false;}
        if(!(obj instanceof Vendeur)){return false;}
        Vendeur tmp = (Vendeur) obj;
        return this.idVendeur == tmp.idVendeur;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idVendeur);
    }

}
