package fr.saejava;

public class LivrePasDansStockMagasinException extends Exception{

    private static final String message = "Le livre n'est pas dans le stock du magasin, il viendra peut-être prochainement.";

    public LivrePasDansStockMagasinException() {
        super(message);
    }

    public LivrePasDansStockMagasinException(String message) {
        super(message);
    }

}
