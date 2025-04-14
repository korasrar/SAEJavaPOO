package fr.saejava;
public class Auteur {
    int idAuteur;
    String nomAuteur;
    int anneeNais;
    int anneeDeces;

    Auteur(int idAuteur,String nomAuteur){
        this.idAuteur=idAuteur;
        this.nomAuteur=nomAuteur;
    }
    Auteur(int idAuteur,String nomAuteur,int anneeNais,int anneeDeces){
        this.idAuteur=idAuteur;
        this.nomAuteur=nomAuteur;
        this.anneeNais=anneeNais;
        this.anneeDeces=anneeDeces;
    }
    public int getIdAuteur() {
        return idAuteur;
    }
    public String getNomAuteur() {
        return nomAuteur;
    }
    public int getAnneeNais() {
        return anneeNais;
    }
    public int getAnneeDeces() {
        return anneeDeces;
    }
    public void setIdAuteur(int idAuteur) {
        this.idAuteur = idAuteur;
    }
    public void setNomAuteur(String nomAuteur) {
        this.nomAuteur = nomAuteur;
    }
    public void setAnneeNais(int anneeNais) {
        this.anneeNais = anneeNais;
    }
    public void setAnneeDeces(int anneeDeces) {
        this.anneeDeces = anneeDeces;
    }
    @Override
    public String toString() {
        return this.idAuteur+" "+this.nomAuteur;
    }

}