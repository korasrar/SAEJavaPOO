package fr.saejava;

import java.sql.SQLException;

public class Vendeur extends Utilisateur{
    
    private int idVendeur;
    private Magasin magasin;
    private MagasinBD magasinBD;

    public Vendeur(int idVendeur, String nom, String prenom,String username, String motDePasse, Magasin magasin, MagasinBD magasinBD){
        super(nom, prenom, username, motDePasse);
        this.idVendeur = idVendeur;
        this.magasin = magasin;
        this.magasinBD = magasinBD;
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

    public void ajouteLivre(Livre l) throws SQLException{ //execption a corriger
        magasinBD.ajoutStock(magasin, l, 1);
        // Appelle ajouteStock de magasin et qte = 1 par default
    }

    public void ajouteLivre(Livre l, int qte) throws SQLException{ //execption a corriger
        magasinBD.ajoutStock(magasin, l, qte);
        // Appelle ajouteStock de magasin
    }

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
