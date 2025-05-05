package fr.saejava;

public class DetailCommande {
    private int numLig;
    private int qte;
    private double prixVente;
    private Livre livre;

    DetailCommande(int numLig, int qte, double prixVente, Livre livre){
        this.numLig = numLig;
        this.qte = qte;
        this.prixVente = prixVente;
        this.livre = livre;
    }
    public int getNumLig(){
        return this.numLig;
    }
    public int getQte(){
        return this.qte;
    }
    public double getPrixVente(){
        return this.prixVente;
    }
    public Livre getLivre(){
        return this.livre;
    }
    public void setNumLig(int numLig){
        this.numLig = numLig;
    }
    public void setQte(int qte){
        this.qte = qte;
    }
    public void setPrixVente(int prixVente){
        this.prixVente = prixVente;
    }
    public void setLivre(Livre livre){
        this.livre = livre;
    }
    @Override
    public String toString(){
        return qte+" "+this.livre+" "+this.prixVente;
    }
    @Override
    public boolean equals(Object obj){
        if(obj==null){return false;}
        if(obj==this){return true;}
        if(!(obj instanceof DetailCommande)){return false;}
        DetailCommande detailCommande=(DetailCommande) obj;
        return this.numLig==detailCommande.getNumLig() && this.livre.equals(detailCommande.getLivre()) && this.prixVente==detailCommande.getPrixVente() && this.qte==detailCommande.getQte();
    }
}
