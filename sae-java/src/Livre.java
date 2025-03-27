public class Livre {
    int isbn;
    String titre;
    int nbpages;
    String datepubli;
    double prix;

    public Livre(int isbn, String titre, int nbpages, String datepubli, double prix) {
        this.isbn = isbn;
        this.titre = titre;
        this.nbpages = nbpages;
        this.datepubli = datepubli;
        this.prix = prix;
    }

    public int getISBN(){
        return this.isbn;
    }
}
