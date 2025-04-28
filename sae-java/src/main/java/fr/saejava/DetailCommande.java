package fr.saejava;

public class DetailCommande {
    Livre livre;
    int qte;
    double prixVente;

    DetailCommande(Livre livre,int qte,double prixVente){
        this.livre=livre;
        this.qte=qte;
    }

    Livre getLivre(){
        return this.livre;
    }
    int getQte(){
        return this.qte;
    }
    void setLivre(Livre livre){
        this.livre=livre;
    }
    void setQte(int qte){
        this.qte=qte;
    }
    @Override
    public String toString(){
        return qte+" "+this.livre+" "+this.prixVente;
    }
}
