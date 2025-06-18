package fr.saejava.model;

public class MotDePasseIncorrectException extends Exception {

    private static final String message = "Le mot de passe est incorrect, veuillez réeessayer.";

    public MotDePasseIncorrectException() {
        super(message);
    }
    public MotDePasseIncorrectException(String message) {
        super(message);
    }
}
