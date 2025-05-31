package fr.saejava;

public class DetailCommande {
    private int numLig;
    private int qte;
    private double prixVente;
    private Livre livre;
    private Commande commande;

    DetailCommande(int qte, Livre livre, Commande commande){
        this.commande = commande;
        this.numLig = commande.getContenue().size()+1;
        this.qte = qte;
        this.livre = livre;
        this.prixVente = livre.getPrix()*qte;
    }

    DetailCommande( Livre livre, Commande commande){ // constructeur qte de 1 par defaut
        this.commande = commande;
        this.numLig = commande.getContenue().size()+1;
        this.qte = 1;
        this.livre = livre;
        this.prixVente = livre.getPrix()*qte;
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

    public Commande getCommandeParent(){
        return this.commande;
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

    @Override
    public int hashCode() {
        return Integer.hashCode(numLig) + livre.hashCode() + Double.hashCode(prixVente) + Integer.hashCode(qte);
    }
}
