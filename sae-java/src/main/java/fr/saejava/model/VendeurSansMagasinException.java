package fr.saejava.model;

public class VendeurSansMagasinException extends Exception{
    private static final String message = "Le vendeur n'a pas de magasin.";

    public VendeurSansMagasinException() {
        super(message);
    }

    public VendeurSansMagasinException(String message) {
        super(message);
    }
    
}
