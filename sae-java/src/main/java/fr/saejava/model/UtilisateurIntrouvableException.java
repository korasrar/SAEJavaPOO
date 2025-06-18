package fr.saejava.model;

public class UtilisateurIntrouvableException extends Exception{
    private static final String message = "L'utilisateur n'a pas été trouvé, veuillez recommencer.";

    public UtilisateurIntrouvableException() {
        super(message);
    }

    public UtilisateurIntrouvableException(String message) {
        super(message);
    }
    
}
