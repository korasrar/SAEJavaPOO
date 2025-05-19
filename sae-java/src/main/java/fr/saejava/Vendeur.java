package fr.saejava;

public class Vendeur extends Utilisateur{
    
    private int idVendeur;
    private Magasin magasin;

    public Vendeur(int idVendeur, String nom, String prenom,String pseudo, String motDePasse, Magasin magasin){
        super(nom, prenom, pseudo, motDePasse);
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

    public void ajouteLivre(Livre l){
        // Appelle ajouteStock de magasin et qte = 1 par default
    }

    public void ajouteLivre(Livre l, int qte){
        // Appelle ajouteStock de magasin
    }

}
