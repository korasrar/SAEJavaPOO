package fr.saejava;

public class LivrePasDansStockMagasinExecption extends Exception{

    private static final String message = "Le livre n'est pas dans le stock du magasin, il viendra peut-être prochainement.";

    public LivrePasDansStockMagasinExecption() {
        super(message);
    }

    public LivrePasDansStockMagasinExecption(String message) {
        super(message);
    }

}
